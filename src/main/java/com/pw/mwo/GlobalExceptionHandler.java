package com.pw.mwo;

import com.pw.mwo.exceptions.ClientNotRegisteredException;
import com.pw.mwo.exceptions.EntityAlreadyExistsException;
import com.pw.mwo.exceptions.EntityNotFoundException;
import com.pw.mwo.exceptions.ProductNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Void> handleException(EntityAlreadyExistsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleException(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ClientNotRegisteredException.class)
    public ResponseEntity<Void> handleException(ClientNotRegisteredException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<String> handleException(ProductNotAvailableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.notFound().build();
    }
}
