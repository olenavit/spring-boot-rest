package ua.com.vitkovska.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.vitkovska.commons.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTeamDto {
    int id;
    @NotBlank(message = Constants.Team.ValidationMessages.NAME_BLANK)
    private String name;
}
