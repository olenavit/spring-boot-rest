package ua.com.vitkovska.dto.player;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.vitkovska.commons.Constants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerQueryDto {
    @Pattern(regexp = Constants.Player.NAME_PATTERN,message = Constants.Player.ValidationMessages.NAME_PATTERN_NOT_VALID)
    private String name;
    @Pattern(regexp = Constants.Player.SURNAME_PATTERN,message = Constants.Player.ValidationMessages.SURNAME_PATTERN_NOT_VALID)
    private String surname;
    private int page;
    private int size;
}
