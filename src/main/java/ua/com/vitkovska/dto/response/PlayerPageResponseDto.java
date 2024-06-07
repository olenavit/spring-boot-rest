package ua.com.vitkovska.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.com.vitkovska.dto.player.PlayerInfoDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlayerPageResponseDto {
    private List<PlayerInfoDto> list;
    private Integer totalPages;
    private Long totalElements;
}
