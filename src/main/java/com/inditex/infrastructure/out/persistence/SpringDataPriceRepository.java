package com.inditex.infrastructure.out.persistence;

import com.inditex.infrastructure.out.entity.PricePersistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPriceRepository extends JpaRepository<PricePersistence, Long> {
    List<PricePersistence> findByProductIdAndBrandId(Long productId, Long brandId);
}
