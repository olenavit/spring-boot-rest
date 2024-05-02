package ua.com.vitkovska.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.validation.UniqueTeam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTeamDto {
    int id;
    @NotBlank(message = Constants.Team.ValidationMessages.NAME_BLANK)
    @UniqueTeam
    private String name;
}
