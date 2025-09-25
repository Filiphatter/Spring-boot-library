package com.example.librarysystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Hanterar ResponseStatusException (kastas ofta i service-lagret)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Loggar internt info för mig
        logger.warn("ResponseStatusException: status={} reason={}", ex.getStatusCode(), ex.getReason());
        // Visa bara användarvänligt felmeddelande
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    // Hanterar alla andra exception → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Loggar intern info för mig
        logger.error("Oväntat fel inträffade", ex);
        // Visa generellt meddelande till användaren
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ett oväntat fel inträffade. Försök igen senare.");
    }
}