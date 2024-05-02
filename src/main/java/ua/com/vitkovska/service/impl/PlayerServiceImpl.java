package ua.com.vitkovska.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vitkovska.dao.PlayerDao;
import ua.com.vitkovska.dto.response.UploadJsonResponseDto;
import ua.com.vitkovska.dto.player.CreatePlayerDto;
import ua.com.vitkovska.dto.player.PlayerDetailsDto;
import ua.com.vitkovska.dto.player.PlayerInfoDto;
import ua.com.vitkovska.dto.player.PlayerQueryDto;
import ua.com.vitkovska.dto.player.UpdatePlayerDto;
import ua.com.vitkovska.dto.team.TeamDto;
import ua.com.vitkovska.exceptions.PlayerNotFoundException;
import ua.com.vitkovska.exceptions.TeamNotFoundException;
import ua.com.vitkovska.mapper.PlayerMapper;
import ua.com.vitkovska.mapper.TeamMapper;
import ua.com.vitkovska.model.Player;
import ua.com.vitkovska.model.Team;
import ua.com.vitkovska.service.PlayerService;
import ua.com.vitkovska.service.TeamService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerDao playerDao;
    private final PlayerMapper playerMapper;
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final Validator validator;

    @Override
    public PlayerDetailsDto create(CreatePlayerDto createPlayerDto) {
        Integer teamId = createPlayerDto.getTeamId();
        Team team;
        if (teamId == null) {
            team = null;
        } else {
            team = getPlayerTeamById(teamId);
        }

        Player createdPlayer = playerDao.save(playerMapper.fromCreatePlayerDto(createPlayerDto, team));
        return playerMapper.toPlayerDetailsDto(createdPlayer);
    }

    @Override
    public Optional<PlayerDetailsDto> getById(int id) {
        Optional<Player> playerOptional = playerDao.findById(id);
        return playerMapper.toPlayerDetailsDtoOptional(playerOptional);
    }

    @Override
    public PlayerDetailsDto update(UpdatePlayerDto updatePlayerDto) {
        Optional<Player> optionalPlayer = playerDao.findById(updatePlayerDto.getId());
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(updatePlayerDto.getId());
        }
        Player player = optionalPlayer.get();
        if (updatePlayerDto.getTeamId() != null) {
            Team team = getPlayerTeamById(updatePlayerDto.getTeamId());
            player.setTeam(team);
        }
        copyNonNullProperties(updatePlayerDto, player);
        player = playerDao.save(player);
        return playerMapper.toPlayerDetailsDto(player);
    }

    @Override
    public boolean deleteById(int id) {
        if (getById(id).isEmpty()) {
            return false;
        }
        playerDao.deleteById(id);
        return true;
    }

    @Override
    public Page<PlayerInfoDto> list(PlayerQueryDto entity) {
        PageRequest pageRequest = PageRequest.of(entity.getPage() - 1, entity.getSize());
        Specification<Player> playerSpecification = getPlayerSpecification(entity);
        Page<Player> players = playerDao.findAll(playerSpecification, pageRequest);
        return players.map(playerMapper::toPlayerInfoDto);
    }

    public List<PlayerInfoDto> report(PlayerQueryDto entity) {
        Specification<Player> playerSpecification = getPlayerSpecification(entity);
        List<Player> players = playerDao.findAll(playerSpecification);
        return players.stream().map(playerMapper::toPlayerInfoDto).toList();
    }

    @Override
    public void generateReport(HttpServletResponse response, PlayerQueryDto playerQueryDto) {

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.csv");
        List<PlayerInfoDto> students = report(playerQueryDto);
        try (PrintWriter writer = response.getWriter()) {
            var builder = new StatefulBeanToCsvBuilder<PlayerInfoDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();
            builder.write(students);
        } catch (IOException e) {
            System.out.println("oo");
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UploadJsonResponseDto uploadFromFile(MultipartFile multipart) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonNode tree = objectMapper.readTree(multipart.getBytes());

        int numOfCreatedPlayers = 0;
        int numOfNotCreatedPlayers = 0;

        for (JsonNode jsonNode : tree) {
            try {
                CreatePlayerDto createPlayerDto = objectMapper.treeToValue(jsonNode, CreatePlayerDto.class);
                Set<ConstraintViolation<CreatePlayerDto>> violations = validator.validate(createPlayerDto);

                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
                create(createPlayerDto);
                numOfCreatedPlayers++;
            } catch (Exception e) {
                numOfNotCreatedPlayers++;
            }
        }

        return new UploadJsonResponseDto(numOfCreatedPlayers, numOfNotCreatedPlayers);
    }


    private Team getPlayerTeamById(int teamId) {
        Optional<TeamDto> teamDtoOptional = teamService.getById(teamId);
        if (teamDtoOptional.isEmpty()) {
            throw new TeamNotFoundException(teamId);
        }
        return teamMapper.fromTeamDto(teamDtoOptional.get());
    }

    private void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private Specification<Player> getPlayerSpecification(PlayerQueryDto entity) {
        Specification<Player> specification = Specification.where(null);
        if (entity.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("name"), entity.getName()));
        }
        if (entity.getSurname() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("surname"), entity.getSurname()));
        }
        return specification;
    }
}
