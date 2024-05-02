package ua.com.vitkovska.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.vitkovska.commons.Constants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayerInfoDto {
    @NotBlank(message = Constants.Player.ValidationMessages.NAME_BLANK)
    @Pattern(regexp = Constants.Player.NAME_PATTERN, message = Constants.Player.ValidationMessages.NAME_PATTERN_NOT_VALID)
    private String name;
    @NotBlank(message = Constants.Player.ValidationMessages.SURNAME_BLANK)
    @Pattern(regexp = Constants.Player.SURNAME_PATTERN,message = Constants.Player.ValidationMessages.SURNAME_PATTERN_NOT_VALID)
    private String surname;
}
