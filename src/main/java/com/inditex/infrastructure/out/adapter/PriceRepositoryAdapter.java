package com.inditex.infrastructure.out.adapter;

import com.inditex.application.port.out.PriceRepository;
import com.inditex.domain.entity.Price;
import com.inditex.infrastructure.out.entity.PricePersistence;
import com.inditex.infrastructure.out.mapper.PriceToPersistenceMapper;
import com.inditex.infrastructure.out.persistence.PriceSpecifications;
import com.inditex.infrastructure.out.persistence.SpringDataPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final SpringDataPriceRepository springDataPriceRepository;

    @Override
    public List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate) {

        Specification<PricePersistence> priceSpecifications =
                PriceSpecifications.applicablePrices(productId, brandId, applicationDate);

        List<PricePersistence> prices = springDataPriceRepository.findAll(priceSpecifications);

        return prices.stream()
                .map(PriceToPersistenceMapper::toPrice)
                .toList();
    }
}
