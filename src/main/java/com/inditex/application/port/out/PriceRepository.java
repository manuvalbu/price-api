package com.inditex.application.port.out;

import com.inditex.domain.entity.Price;
import java.util.List;

public interface PriceRepository {

    List<Price> findPrices(Long productId, Long brandId);
}
