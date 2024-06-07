package ua.com.vitkovska.dto.player;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.vitkovska.commons.Constants;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UpdatePlayerDto {
    private int id;
    @Pattern(regexp = Constants.Player.NAME_PATTERN, message = Constants.Player.ValidationMessages.NAME_PATTERN_NOT_VALID)
    private String name;
    @Pattern(regexp = Constants.Player.SURNAME_PATTERN,message = Constants.Player.ValidationMessages.SURNAME_PATTERN_NOT_VALID)
    private String surname;
    @Min(value = Constants.Player.MIN_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MIN_YEAR_OF_BIRTH_NOT_VALID)
    @Max(value = Constants.Player.MAX_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MAX_YEAR_OF_BIRTH_NOT_VALID)
    private Integer yearOfBirth;
    private List<String> position;
    private Integer teamId;
}