package com.inditex.domain.service;

import com.inditex.domain.entity.Price;
import com.inditex.domain.exception.PriceNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PriceResolver {

    public Price resolve(LocalDateTime requestedDateTime, List<Price> availablePrices) {

        Objects.requireNonNull(availablePrices, "Price List cannot be null");
        Objects.requireNonNull(requestedDateTime, "Date cannot be null");

        return availablePrices.stream()
                // Keep only applicable prices
                .filter(price -> price.isApplicable(requestedDateTime))
                // Select the one with the highest priority
                .max(Comparator.comparing(Price::priority))
                // If none found → throw domain exception
                .orElseThrow(() -> new PriceNotFoundException(requestedDateTime));
    }
}
