package ua.com.vitkovska.service;

import ua.com.vitkovska.dto.team.CreateTeamDto;
import ua.com.vitkovska.dto.team.TeamDto;
import ua.com.vitkovska.dto.team.UpdateTeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<TeamDto> findAll();

    TeamDto create(CreateTeamDto entity);

    Optional<TeamDto> getById(int id);

    TeamDto update(UpdateTeamDto entity);

    boolean deleteById(int id);

    boolean existsByTeamName(String name);

}
