package com.inditex.domain.entity;

import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public record Price(
        Long productId,
        Long brandId,
        Integer priceList,
        Integer priority,
        BigDecimal price,
        Currency currency,
        DateRange dateRange
) {

    public Price {
        Objects.requireNonNull(productId, "productId cannot be null");
        Objects.requireNonNull(brandId, "brandId cannot be null");
        Objects.requireNonNull(priceList, "priceList cannot be null");
        Objects.requireNonNull(priority, "priority cannot be null");
        Objects.requireNonNull(price, "price cannot be null");
        Objects.requireNonNull(currency, "currency cannot be null");
        Objects.requireNonNull(dateRange, "dateRange cannot be null");

        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
        if (brandId <= 0) {
            throw new IllegalArgumentException("brandId must be positive");
        }
        if (priceList <= 0) {
            throw new IllegalArgumentException("priceList must be positive");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("priority cannot be negative");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
    }

    public boolean isApplicable(LocalDateTime date) {
        return dateRange.contains(date);
    }
}
