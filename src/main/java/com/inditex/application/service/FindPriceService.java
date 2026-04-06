package com.inditex.application.service;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.dto.PriceResponse;
import com.inditex.application.mapper.PriceToResponseMapper;
import com.inditex.application.port.in.FindPriceUseCase;
import com.inditex.application.port.out.PriceRepository;
import com.inditex.domain.entity.Price;
import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.domain.service.PriceResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindPriceService implements FindPriceUseCase {

    private final PriceRepository priceRepository;
    private final PriceResolver priceResolver = new PriceResolver();

    public FindPriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceResponse execute(PriceQuery priceQuery) {
        List<Price> availablePrices = priceRepository.findPrices(priceQuery.productId(), priceQuery.brandId());
        if (availablePrices.isEmpty())
            throw new PriceNotFoundException("price not found for product " + priceQuery.productId() + " and brand " + priceQuery.brandId());
        Price priceFound = priceResolver.resolve(priceQuery.date(), availablePrices);
        return PriceToResponseMapper.toResponse(priceFound);
    }
}
