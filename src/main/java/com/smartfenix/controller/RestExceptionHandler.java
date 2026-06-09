package com.smartfenix.controller;

import com.smartfenix.exception.RegistroNoEncontradoException;
import com.smartfenix.exception.RegistroRelacionadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RegistroNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RegistroNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RegistroRelacionadoException.class)
    public ResponseEntity<Map<String, String>> handleConflict(RegistroRelacionadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
}
