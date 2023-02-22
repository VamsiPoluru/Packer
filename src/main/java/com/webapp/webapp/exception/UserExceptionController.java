package com.webapp.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {
    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<?> exception(UserException exception) {
        return new ResponseEntity<>("Username already in use.", HttpStatus.BAD_REQUEST);
    }
}
