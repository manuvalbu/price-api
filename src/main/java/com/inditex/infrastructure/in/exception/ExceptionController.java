package com.inditex.infrastructure.in.exception;

import com.inditex.domain.exception.PriceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ExceptionController {

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlePriceNotFoundException(final PriceNotFoundException e) {
        log.warn("Price not found: {}", e.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDto> handleMissingParameter(final MissingServletRequestParameterException e) {
        log.warn("Missing request parameter: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleTypeMismatch(final MethodArgumentTypeMismatchException e) {
        String message = "Invalid value for parameter '" + e.getName() + "'";
        log.warn("Type mismatch on parameter '{}': rejected value={}", e.getName(), e.getValue());
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionDto> handleUnknownException(
            final Exception e) {
        log.error("Unexpected error", e);
        ExceptionDto exceptionDto = ExceptionDto
                .builder()
                .code(Exception.class.getName())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    private ResponseEntity<ExceptionDto> buildResponse(HttpStatus status, String message) {
        ExceptionDto body = ExceptionDto.builder()
                .code(status.name())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
