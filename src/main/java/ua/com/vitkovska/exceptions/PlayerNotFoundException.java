package ua.com.vitkovska.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.com.vitkovska.commons.Constants;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    private static final String MESSAGE = Constants.Exceptions.PLAYER_NOT_FOUND_MESSAGE;
    public PlayerNotFoundException(int id) {
        super(String.format(MESSAGE,id));
    }
}