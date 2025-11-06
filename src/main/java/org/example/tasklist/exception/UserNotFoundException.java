package org.example.tasklist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User is not Found")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super("UserNotFoundException: " + message);
        log.info("caught by TaskNotFoundException");
    }

}
