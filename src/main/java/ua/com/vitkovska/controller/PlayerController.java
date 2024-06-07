package ua.com.vitkovska.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.dto.player.CreatePlayerDto;
import ua.com.vitkovska.dto.player.PlayerDetailsDto;
import ua.com.vitkovska.dto.player.PlayerInfoDto;
import ua.com.vitkovska.dto.player.PlayerQueryDto;
import ua.com.vitkovska.dto.player.UpdatePlayerDto;
import ua.com.vitkovska.dto.response.PlayerPageResponseDto;
import ua.com.vitkovska.dto.response.UploadJsonResponseDto;
import ua.com.vitkovska.exceptions.EntityNotFoundException;
import ua.com.vitkovska.service.PlayerService;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping(Constants.Path.PLAYER_API)
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    @GetMapping(Constants.Path.ID_PATH_VARIABLE)
    public EntityModel<PlayerDetailsDto> getPlayerById(@PathVariable int id){
        Optional<PlayerDetailsDto> optionalPlayerDetailsDto = playerService.getById(id);
        if(optionalPlayerDetailsDto.isEmpty()){
            throw new EntityNotFoundException(id,Constants.Player.ENTITY_NAME);
        }
        return EntityModel.of(optionalPlayerDetailsDto.get());
    }

    @PostMapping
    public ResponseEntity<PlayerDetailsDto> createPlayer(@Valid @RequestBody CreatePlayerDto createPlayerDto) {
        PlayerDetailsDto createdTeam = playerService.create(createPlayerDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(Constants.Path.ID_PATH_VARIABLE)
                .buildAndExpand(createdTeam.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTeam);
    }
    @PutMapping(Constants.Path.ID_PATH_VARIABLE)
    public ResponseEntity<PlayerDetailsDto> updatePlayer(@PathVariable int id, @Valid @RequestBody UpdatePlayerDto dto) {
        dto.setId(id);
        PlayerDetailsDto updatedPlayer = playerService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPlayer);
    }
    @DeleteMapping(Constants.Path.ID_PATH_VARIABLE)
    public ResponseEntity<String> deleteTeamById(@PathVariable int id) {
        if (playerService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(Constants.Path.UPLOAD)
    public EntityModel<UploadJsonResponseDto> uploadFromFile(@RequestParam("file") MultipartFile multipart) throws IOException {
        return EntityModel.of(playerService.uploadFromFile(multipart));
    }

    @PostMapping(Constants.Path.LIST)
    public EntityModel<PlayerPageResponseDto> getPlayersByGivenFieldsAndPages(@Valid @RequestBody PlayerQueryDto playerQueryDto){
        Page<PlayerInfoDto> playerInfoDtos = playerService.list(playerQueryDto);
        PlayerPageResponseDto playerPageResponseDto = new PlayerPageResponseDto(playerInfoDtos.stream().toList(), playerInfoDtos.getTotalPages(),playerInfoDtos.getTotalElements());
       return EntityModel.of(playerPageResponseDto);
    }

    @PostMapping(Constants.Path.REPORT)
    public void getPlayersByGivenField(@Valid @RequestBody PlayerQueryDto playerQueryDto, HttpServletResponse httpServletResponse){
       playerService.generateReport(httpServletResponse,playerQueryDto);
    }

}
