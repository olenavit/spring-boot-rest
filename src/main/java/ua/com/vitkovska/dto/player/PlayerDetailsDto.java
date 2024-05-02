package ua.com.vitkovska.dto.player;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import  ua.com.vitkovska.commons.Constants;

import ua.com.vitkovska.model.Team;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PlayerDetailsDto {
    private Integer id;
    @NotBlank(message = Constants.Player.ValidationMessages.NAME_BLANK)
    @Pattern(regexp = Constants.Player.NAME_PATTERN, message = Constants.Player.ValidationMessages.NAME_PATTERN_NOT_VALID)
    private String name;
    @NotBlank(message = Constants.Player.ValidationMessages.SURNAME_BLANK)
    @Pattern(regexp = Constants.Player.SURNAME_PATTERN, message = Constants.Player.ValidationMessages.SURNAME_PATTERN_NOT_VALID)
    private String surname;
    @Min(value = Constants.Player.MIN_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MIN_YEAR_OF_BIRTH_NOT_VALID)
    @Max(value = Constants.Player.MAX_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MAX_YEAR_OF_BIRTH_NOT_VALID)
    private Integer yearOfBirth;
    private List<String> position;
    private Team team;
}

