package ua.com.vitkovska.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.message.EmailDetailsMessage;
import ua.com.vitkovska.dto.team.CreateTeamDto;
import ua.com.vitkovska.dto.team.TeamDto;
import ua.com.vitkovska.dto.team.UpdateTeamDto;
import ua.com.vitkovska.exceptions.EntityNotFoundException;
import ua.com.vitkovska.service.MessageSenderService;
import ua.com.vitkovska.service.TeamService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(Constants.Path.TEAM_API)
public class TeamController {

    private final TeamService teamService;
    private final MessageSenderService messageSenderService;

    @GetMapping
    public List<TeamDto> getTeams() {
        return teamService.findAll();
    }

    @GetMapping(Constants.Path.ID_PATH_VARIABLE)
    public EntityModel<TeamDto> getTeamById(@PathVariable int id) {
        Optional<TeamDto> teamDtoOptional = teamService.getById(id);
        if (teamDtoOptional.isEmpty()) {
            throw new EntityNotFoundException(id, Constants.Team.ENTITY_NAME);
        }
        EntityModel<TeamDto> teamDtoEntityModel = EntityModel.of(teamDtoOptional.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getTeams());
        teamDtoEntityModel.add(link.withRel("all-teams"));
        return teamDtoEntityModel;
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@Valid @RequestBody CreateTeamDto teamDto) {
        TeamDto createdTeam = teamService.create(teamDto);

        EmailDetailsMessage emailDetailsMessage = new EmailDetailsMessage(
                Constants.Message.Team.TEAM_CREATED_SUBJECT,
                String.format(Constants.Message.Team.TEAM_CREATED_CONTENT, createdTeam.getName()));
        messageSenderService.sendMessage(emailDetailsMessage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(Constants.Path.ID_PATH_VARIABLE)
                .buildAndExpand(createdTeam.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTeam);
    }

    @PutMapping(Constants.Path.ID_PATH_VARIABLE)
    public ResponseEntity<TeamDto> updateTeam(@PathVariable int id, @Valid @RequestBody TeamDto teamDto) {
        UpdateTeamDto updateTeamDto = new UpdateTeamDto(id, teamDto.getName());
        TeamDto updatedTeam = teamService.update(updateTeamDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
    }


    @DeleteMapping(Constants.Path.ID_PATH_VARIABLE)
    public ResponseEntity<String> deleteTeamById(@PathVariable int id) {
        if (teamService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
