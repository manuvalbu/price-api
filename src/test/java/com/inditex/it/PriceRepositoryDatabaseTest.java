package com.inditex.it;

import com.inditex.PriceApplication;
import com.inditex.infrastructure.out.entity.PricePersistence;
import com.inditex.infrastructure.out.persistence.SpringDataPriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {PriceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceRepositoryDatabaseTest {

    @Autowired
    SpringDataPriceRepository springDataPriceRepository;

    @Test
    void searchPricesOk_IT() {
        Long productId = 35455L;
        Long brandId = 1L;
        List<PricePersistence> pricePersistenceAllList = springDataPriceRepository.findAll();
        List<PricePersistence> pricePersistenceFilteredList = springDataPriceRepository.findByProductIdAndBrandId(productId, brandId);

        assertTrue(pricePersistenceAllList.size() >= pricePersistenceFilteredList.size());
        assertFalse(pricePersistenceFilteredList.isEmpty());
        assertEquals(productId, pricePersistenceFilteredList.get(0).getProductId());
        assertEquals(brandId, pricePersistenceFilteredList.get(0).getBrandId());
    }
}
