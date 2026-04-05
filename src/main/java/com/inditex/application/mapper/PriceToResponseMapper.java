package com.inditex.application.mapper;

import com.inditex.application.dto.PriceResponse;
import com.inditex.domain.entity.Price;

public class PriceToResponseMapper {

    public static PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .productId(price.productId())
                .brandId(price.brandId())
                .priceList(price.priceList())
                .price(price.price())
                .startDate(price.dateRange().start())
                .endDate(price.dateRange().end())
                .build();
    }
}
