package com.inditex.application.mapper;

import com.inditex.application.dto.PriceResponse;
import com.inditex.domain.entity.Price;

public class PriceMapper {

    public static PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.dateRange().start(),
                price.dateRange().end(),
                price.price()
        );
    }
}
