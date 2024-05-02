package ua.com.vitkovska.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.vitkovska.service.TeamService;

@Component
@RequiredArgsConstructor
public class UniqueTeamValidator implements ConstraintValidator<UniqueTeam, String> {

    private final TeamService teamService;

    @Override
    public boolean isValid(String teamName, ConstraintValidatorContext context) {
        return !teamService.existsByTeamName(teamName);
    }

}
