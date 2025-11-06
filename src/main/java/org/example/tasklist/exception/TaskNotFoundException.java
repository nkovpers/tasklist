package org.example.tasklist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Task is not Found")
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String message) {
        super("TaskNotFoundException: " + message);
        log.info("caught by TaskNotFoundException");
    }

}
