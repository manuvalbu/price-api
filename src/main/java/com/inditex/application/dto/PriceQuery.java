package com.inditex.application.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record PriceQuery(
        Long productId,
        Long brandId,
        LocalDateTime date
) {
}
