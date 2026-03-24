package com.inditex.domain.entity;

import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
        Long productId,
        Long brandId,
        Integer priceList,
        Integer priority,
        BigDecimal price,
        Currency currency,
        DateRange dateRange
) {

    public static Price of(
            long productId, long brandId,
            int priceList, int priority,
            String priceStr, String currency,
            String start, String end) {

        return new Price(
                productId, brandId, priceList, priority,
                new BigDecimal(priceStr),
                Currency.of(currency),
                DateRange.of(LocalDateTime.parse(start), LocalDateTime.parse(end))
        );
    }

    public boolean isApplicable(LocalDateTime date) {
        return dateRange.contains(date);
    }
}
