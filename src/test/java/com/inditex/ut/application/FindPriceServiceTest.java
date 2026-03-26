package com.inditex.ut.application;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.port.out.PriceRepository;
import com.inditex.application.service.FindPriceService;
import com.inditex.domain.entity.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindPriceServiceTest {

    private FindPriceService findPriceService;

    @Mock
    private PriceRepository priceRepositoryMock;

    @BeforeEach
    void setUp() {
        findPriceService = new FindPriceService(priceRepositoryMock);
    }

    @Test
    @DisplayName("Using service test")
    void using_service_test() {
        long productId = 1;
        long brandId = 1;
        int priceList = 3;
        BigInteger priceValue = BigInteger.valueOf(35);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(2);
        Price price = Price.of(productId, brandId, priceList, 0, priceValue.toString(), "EUR", startDate.toString(), endDate.toString());
        PriceQuery priceQuery = new PriceQuery(productId,brandId,startDate.plusHours(1));
        when(priceRepositoryMock.findPrices(productId,brandId)).thenReturn(List.of(price));

        var result = findPriceService.execute(priceQuery);

        assertThat(result.priceList()).isEqualTo(priceList);
        assertThat(result.price()).isEqualTo(priceValue.toString());
    }
}
