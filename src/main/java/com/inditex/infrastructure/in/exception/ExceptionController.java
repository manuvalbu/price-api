package com.inditex.infrastructure.in.exception;

import com.inditex.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ExceptionController {

    @ExceptionHandler({PriceNotFoundException.class})
    public ResponseEntity<ExceptionDto> handlePriceNotFoundException(
            final PriceNotFoundException e) {
        ExceptionDto exceptionDto = ExceptionDto
                .builder()
                .code(ExceptionDto.class.getName())
                .message(e.getMessage())
                .build();
        log.info(exceptionDto.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionDto);
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
}
