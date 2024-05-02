package ua.com.vitkovska.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.com.vitkovska.commons.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueTeamValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTeam {
    String message() default Constants.Team.ValidationMessages.NAME_NOT_UNIQUE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
