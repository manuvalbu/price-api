package com.inditex.application.port.out;

import com.inditex.domain.entity.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate);
}
