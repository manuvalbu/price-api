package com.inditex.domain.vo;

import java.util.Objects;

public record Currency(String code) {

    public Currency {
        Objects.requireNonNull(code, "Currency code cannot be null");

        if (code.length() != 3) {
            throw new IllegalArgumentException("Currency must be ISO-4217 (3 letters)");
        }
    }

    public static Currency of(String code) {
        return new Currency(code);
    }
}
