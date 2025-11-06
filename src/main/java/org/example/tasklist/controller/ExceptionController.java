package org.example.tasklist.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tasklist.exception.TaskNotFoundException;
import org.example.tasklist.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleTaskException(Exception ex, WebRequest request) {
        log.info("caught by ExceptionController");
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "ExceptionController: " + ex.getMessage());
//        pd.setType(URI.create(((ServletWebRequest) request).getRequest().getRequestURL().toString()));
//        pd.setTitle("Record Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }
}
