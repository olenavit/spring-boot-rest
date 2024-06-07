package ua.com.vitkovska.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.com.vitkovska.commons.Constants;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = Constants.Exceptions.ENTITY_NOT_FOUND_MESSAGE;
    public EntityNotFoundException(int id, String entityName) {
        super(String.format(MESSAGE,entityName,id));
    }
}