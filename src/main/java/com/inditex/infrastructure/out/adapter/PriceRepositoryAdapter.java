package com.inditex.infrastructure.out.adapter;

import com.inditex.application.port.out.PriceRepository;
import com.inditex.domain.entity.Price;
import com.inditex.infrastructure.out.entity.PricePersistence;
import com.inditex.infrastructure.out.mapper.PriceToPersistenceMapper;
import com.inditex.infrastructure.out.persistence.SpringDataPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final SpringDataPriceRepository springDataPriceRepository;

    @Override
    public List<Price> findPrices(Long productId, Long brandId) {
        List<PricePersistence> pricesPersistence = springDataPriceRepository.findByProductIdAndBrandId(productId, brandId);
        return pricesPersistence.stream().map(PriceToPersistenceMapper::toPrice).toList();
    }
}
