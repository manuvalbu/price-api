package com.inditex.application.service;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.dto.PriceResponse;
import com.inditex.application.mapper.PriceMapper;
import com.inditex.application.port.in.FindPriceUseCase;
import com.inditex.application.port.out.PriceRepository;
import com.inditex.domain.entity.Price;
import com.inditex.domain.service.PriceResolver;

import java.util.List;

public class FindPriceService implements FindPriceUseCase {

    private final PriceRepository priceRepository;
    private final PriceResolver priceResolver = new PriceResolver();

    public FindPriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceResponse execute(PriceQuery priceQuery) {
        List<Price> availablePrices = priceRepository.findPrices(priceQuery.productId(), priceQuery.brandId());
        Price priceFound = priceResolver.resolve(priceQuery.date(), availablePrices);
        return PriceMapper.toResponse(priceFound);
    }
}
