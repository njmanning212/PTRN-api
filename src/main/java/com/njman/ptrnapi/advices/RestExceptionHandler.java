package com.njman.ptrnapi.advices;

import com.njman.ptrnapi.daos.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatusCode().value(), ex.getReason()), ex.getStatusCode());
    }
}
