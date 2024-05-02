package ua.com.vitkovska.dto.player;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.vitkovska.commons.Constants;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect
public class CreatePlayerDto {
    @NotBlank(message = Constants.Player.ValidationMessages.NAME_BLANK)
    @Pattern(regexp = Constants.Player.NAME_PATTERN, message = Constants.Player.ValidationMessages.NAME_PATTERN_NOT_VALID)
    private String name;
    @NotBlank(message = Constants.Player.ValidationMessages.SURNAME_BLANK)
    @Pattern(regexp = Constants.Player.SURNAME_PATTERN,message = Constants.Player.ValidationMessages.SURNAME_PATTERN_NOT_VALID)
    private String surname;
    @Min(value = Constants.Player.MIN_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MIN_YEAR_OF_BIRTH_NOT_VALID)
    @Max(value = Constants.Player.MAX_YEAR_OF_BIRTH, message = Constants.Player.ValidationMessages.MAX_YEAR_OF_BIRTH_NOT_VALID)
    private Integer yearOfBirth;
    private List<String> position;
    private Integer teamId;

    @JsonCreator
    public CreatePlayerDto(@JsonProperty("name") String name,
                           @JsonProperty("surname") String surname,
                           @JsonProperty("year_of_birth") Integer yearOfBirth,
                           @JsonProperty("position") String position,
                           @JsonProperty("teamId") Integer teamId) {
        this.name = name;
        this.surname = surname;
        this.teamId = teamId;
        this.yearOfBirth = yearOfBirth;
        if (position != null) {
            this.position = Arrays.asList(position.split(", "));
        } else {
            this.position = null;
        }
    }
}
