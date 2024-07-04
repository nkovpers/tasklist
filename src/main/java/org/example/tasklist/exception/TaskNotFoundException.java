package org.example.tasklist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Task is not Found")
public class TaskNotFoundException extends Exception{

    public TaskNotFoundException(String message) {
        super(message);
    }

}
