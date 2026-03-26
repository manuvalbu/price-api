package com.inditex.application.dto;

import java.time.LocalDateTime;

public record PriceQuery(
        Long productId,
        Long brandId,
        LocalDateTime date
) {
}
