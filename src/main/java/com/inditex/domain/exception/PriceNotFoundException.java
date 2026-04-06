package com.inditex.domain.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(String msg) {
        super(msg);
    }

    public PriceNotFoundException(LocalDateTime date) {
        super(String.format("No applicable price found for date %s", date));
    }
}
