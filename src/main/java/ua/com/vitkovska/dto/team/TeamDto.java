package ua.com.vitkovska.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.validation.UniqueTeam;

@Builder
@Getter
@Setter
public class TeamDto {
    private int id;
    @NotBlank(message = Constants.Team.ValidationMessages.NAME_BLANK)
    @UniqueTeam
    private String name;
}
