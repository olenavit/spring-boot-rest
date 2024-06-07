package ua.com.vitkovska.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.dao.TeamDao;
import ua.com.vitkovska.dto.team.CreateTeamDto;
import ua.com.vitkovska.dto.team.TeamDto;
import ua.com.vitkovska.dto.team.UpdateTeamDto;
import ua.com.vitkovska.exceptions.EntityNotFoundException;
import ua.com.vitkovska.mapper.TeamMapper;
import ua.com.vitkovska.model.Team;
import ua.com.vitkovska.service.TeamService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamDao teamDao;
    private final TeamMapper teamMapper;

    @Override
    public List<TeamDto> findAll() {
        return teamDao.findAll().stream().map(teamMapper::toTeamDto).toList();
    }

    @Override
    public Optional<TeamDto> getById(int id) {
        Optional<Team> teamOptional = teamDao.findById(id);
        return teamMapper.toTeamDtoOptional(teamOptional);
    }

    @Override
    @Transactional
    public TeamDto create(CreateTeamDto createTeamDto) {
        Team team = teamDao.save(teamMapper.fromCreateTeamDto(createTeamDto));
        return teamMapper.toTeamDto(team);
    }

    @Override
    @Transactional
    public TeamDto update(UpdateTeamDto updateTeamDto) {
        if (getById(updateTeamDto.getId()).isEmpty()) {
            throw new EntityNotFoundException(updateTeamDto.getId(), Constants.Team.ENTITY_NAME);
        }
        Team team = teamDao.save(teamMapper.fromUpdateTeamDto(updateTeamDto));
        return teamMapper.toTeamDto(team);
    }


    @Override
    @Transactional
    public boolean deleteById(int id) {
        if (getById(id).isEmpty()) {
            return false;
        }
        teamDao.deleteById(id);
        return true;
    }

    public boolean existsByTeamName(String teamName) {
        return teamDao.existsByName(teamName);
    }

}
