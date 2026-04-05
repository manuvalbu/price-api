package com.inditex.domain.entity;

import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public boolean isApplicable(LocalDateTime date) {
        return dateRange.contains(date);
    }
}
