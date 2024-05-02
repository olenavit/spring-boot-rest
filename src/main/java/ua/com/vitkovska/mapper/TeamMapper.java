package ua.com.vitkovska.mapper;

import org.springframework.stereotype.Component;
import ua.com.vitkovska.dto.team.CreateTeamDto;
import ua.com.vitkovska.dto.team.TeamDto;
import ua.com.vitkovska.dto.team.UpdateTeamDto;
import ua.com.vitkovska.model.Team;

import java.util.Optional;

@Component
public class TeamMapper {

    public TeamDto toTeamDto(Team team){
        return TeamDto.builder().id(team.getId()).name(team.getName()).build();
    }

    public Optional<TeamDto> toTeamDtoOptional(Optional<Team> optionalTeam) {
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            TeamDto teamDto = TeamDto.builder().id(team.getId()).name(team.getName()).build();
            return Optional.of(teamDto);
        } else {
            return Optional.empty();
        }
    }


    public Team fromCreateTeamDto(CreateTeamDto createTeamDto) {
        return new Team(null, createTeamDto.getName());
    }

    public Team fromUpdateTeamDto(UpdateTeamDto updateTeamDto) {
        return new Team(updateTeamDto.getId(), updateTeamDto.getName());
    }

    public Team fromTeamDto(TeamDto teamDto) {
        return new Team(teamDto.getId(), teamDto.getName());
    }
}
