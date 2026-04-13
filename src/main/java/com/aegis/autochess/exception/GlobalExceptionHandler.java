package com.aegis.autochess.exception;

import com.aegis.autochess.dto.ErrorResponseDto;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientGoldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInsufficientGold(InsufficientGoldException ex) {
        ErrorResponseDto dto = new ErrorResponseDto(
            400, "Bad Request", ex.getMessage(), Instant.now().toString()
        );
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(UnitPoolExhaustedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDto> handleUnitPoolExhausted(UnitPoolExhaustedException ex) {
        ErrorResponseDto dto = new ErrorResponseDto(
            409, "Conflict", ex.getMessage(), Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(UnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleUnitNotFound(UnitNotFoundException ex) {
        ErrorResponseDto dto = new ErrorResponseDto(
            404, "Not Found", ex.getMessage(), Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(InvalidPurchaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInvalidPurchase(InvalidPurchaseException ex) {
        ErrorResponseDto dto = new ErrorResponseDto(
            400, "Bad Request", ex.getMessage(), Instant.now().toString()
        );
        return ResponseEntity.badRequest().body(dto);
    }
}