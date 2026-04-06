package com.inditex.infrastructure.out.mapper;

import com.inditex.domain.entity.Price;
import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import com.inditex.infrastructure.out.entity.PricePersistence;

public class PriceToPersistenceMapper {

    public static Price toPrice(PricePersistence pricePersistence) {
        return Price.builder()
                .productId(pricePersistence.getProductId())
                .brandId(pricePersistence.getBrandId())
                .priceList(pricePersistence.getPriceList())
                .priority(pricePersistence.getPriority())
                .currency(Currency.of(pricePersistence.getCurr()))
                .price(pricePersistence.getPrice())
                .dateRange(DateRange.of(pricePersistence.getStartDate(), pricePersistence.getEndDate()))
                .build();
    }
}
