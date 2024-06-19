package ua.com.vitkovska.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EntityNotUniqueException extends RuntimeException{

    public EntityNotUniqueException(String message) {
        super(message);
    }
}
