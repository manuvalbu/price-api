package com.inditex.infrastructure.in.exception;

import com.inditex.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ExceptionController {

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlePriceNotFoundException(final PriceNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDto> handleMissingParameter(final MissingServletRequestParameterException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleTypeMismatch(final MethodArgumentTypeMismatchException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + e.getName() + "'");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionDto> handleUnknownException(
            final Exception e) {
        ExceptionDto exceptionDto = ExceptionDto
                .builder()
                .code(Exception.class.getName())
                .message(e.getMessage())
                .build();
        log.info(exceptionDto.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    private ResponseEntity<ExceptionDto> buildResponse(HttpStatus status, String message) {
        ExceptionDto body = ExceptionDto.builder()
                .code(status.name())
                .message(message)
                .build();
        log.info(body.toString());
        return ResponseEntity.status(status).body(body);
    }
}
