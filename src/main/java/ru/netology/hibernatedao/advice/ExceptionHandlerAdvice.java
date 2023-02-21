package ru.netology.hibernatedao.advice;

import lombok.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.hibernatedao.exception.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @Value
    private static class ErrorResponse {
        LocalDateTime timestamp = LocalDateTime.now();
        String id = Long.toHexString(System.nanoTime());
        int code;
        String message;
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, NotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }


}
