package com.inditex.it;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.dto.PriceResponse;
import com.inditex.application.port.in.FindPriceUseCase;
import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.domain.vo.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FindPriceUseCase priceServiceMock;

    @Value("${rest.base-url}")
    private String restBaseUrl;
    @Value("${rest.endpoints.price}")
    private String restEndpointPrice;

    Long productId;
    Long brandId;
    LocalDateTime date;

    @BeforeEach
    void setUp() {
        productId = 35455L;
        brandId = 1L;
        date = LocalDateTime.of(2020, 6, 15, 0, 0, 0);
    }


    @Test
    void getPriceOk_IT() throws Exception {
        Integer priceList = 3;
        BigDecimal price = BigDecimal.valueOf(23.5);
        DateRange dateRange = new DateRange(date, date.plusHours(2));
        PriceResponse priceResponse = PriceResponse.builder().productId(productId).brandId(brandId)
                .priceList(priceList).price(price).startDate(dateRange.start()).endDate(dateRange.end())
                .build();
        PriceQuery priceQuery = PriceQuery.builder().productId(productId).brandId(brandId).date(date).build();

        when(priceServiceMock.execute(priceQuery)).thenReturn(priceResponse);

        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("date", date)
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(dateRange.end().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceList").value(priceList))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(price));

        verify(priceServiceMock, times(1)).execute(priceQuery);
    }

    @Test
    void getPriceKoPriceNotFound_IT() throws Exception {
        PriceQuery priceQuery = PriceQuery.builder().productId(productId).brandId(brandId).date(date).build();

        when(priceServiceMock.execute(priceQuery)).thenThrow(new PriceNotFoundException(""));

        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("date", date)
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isNotFound());               ;

        verify(priceServiceMock, times(1)).execute(priceQuery);
    }

    @Test
    @DisplayName("Should return 400 when product_id is missing")
    void getPriceKoMissingProductId_IT() throws Exception {
        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("date", date)
                .queryParam("brand_id", brandId)
                .toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceServiceMock);
    }

    @Test
    @DisplayName("Should return 400 when brand_id is missing")
    void getPriceKoMissingBrandId_IT() throws Exception {
        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("date", date)
                .queryParam("product_id", productId)
                .toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceServiceMock);
    }

    @Test
    @DisplayName("Should return 400 when date is missing")
    void getPriceKoMissingDate_IT() throws Exception {
        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId)
                .toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceServiceMock);
    }

    @Test
    @DisplayName("Should return 400 when date has an invalid format")
    void getPriceKoInvalidDateFormat_IT() throws Exception {
        final String path = UriComponentsBuilder.fromPath(restBaseUrl + restEndpointPrice)
                .queryParam("date", "14-06-2020 10:00")
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId)
                .toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceServiceMock);
    }
}
