package ua.com.vitkovska.mapper;

import org.springframework.stereotype.Component;
import ua.com.vitkovska.dto.player.CreatePlayerDto;
import ua.com.vitkovska.dto.player.PlayerDetailsDto;
import ua.com.vitkovska.dto.player.PlayerInfoDto;
import ua.com.vitkovska.model.Player;
import ua.com.vitkovska.model.Team;

import java.util.Optional;

@Component
public class PlayerMapper {
    public Player fromCreatePlayerDto(CreatePlayerDto createPlayerDto, Team team) {
        return new Player(null,
                createPlayerDto.getName(),
                createPlayerDto.getSurname(),
                createPlayerDto.getYearOfBirth(),
                createPlayerDto.getPosition(),
                team);
    }

    public PlayerDetailsDto toPlayerDetailsDto(Player createdPlayer) {
        return new PlayerDetailsDto(createdPlayer.getId(), createdPlayer.getName(), createdPlayer.getSurname(), createdPlayer.getYearOfBirth(), createdPlayer.getPosition(), createdPlayer.getTeam());
    }

    public Optional<PlayerDetailsDto> toPlayerDetailsDtoOptional(Optional<Player> optionalPlayer) {
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            PlayerDetailsDto playerDetailsDto = new PlayerDetailsDto(player.getId(),
                    player.getName(), player.getSurname(), player.getYearOfBirth(),
                    player.getPosition(), player.getTeam());
            return Optional.of(playerDetailsDto);
        } else {
            return Optional.empty();
        }
    }

    public PlayerInfoDto toPlayerInfoDto(Player player) {
        return new PlayerInfoDto(player.getName(), player.getSurname());
    }
}
