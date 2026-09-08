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
        List<Price> applicablePrices = priceRepository.findApplicablePrices(priceQuery.productId(), priceQuery.brandId(), priceQuery.date());
        if (applicablePrices.isEmpty())
            throw new PriceNotFoundException("price not found for product " + priceQuery.productId() + " brand " + priceQuery.brandId() + "and date" + priceQuery.date().toString());
        Price priceFound = priceResolver.resolve(priceQuery.date(), applicablePrices);
        return PriceToResponseMapper.toResponse(priceFound);
    }
}
