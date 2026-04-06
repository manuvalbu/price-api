package com.inditex.e2e;

import com.inditex.PriceApplication;
import com.inditex.application.dto.PriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = {PriceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceEndPointTest {
    @LocalServerPort
    int randomServerPort;

    RestTemplate restTemplate = new RestTemplate();
    static final String BASE_URL = "http://localhost:";
    @Value("${rest.base-url}")
    private String restBaseUrl;
    @Value("${rest.endpoints.price}")
    private String restEndpointPrice;

    Long productId;
    Long brandId;


    @BeforeEach
    void setUp() {
        productId = 35455L;
        brandId = 1L;
    }

    @ParameterizedTest
    @MethodSource("priceInputs")
    void retrievePriceOKE2ETest(LocalDateTime date, Integer priceListResult, BigDecimal priceResult) {

        final String uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + randomServerPort + restBaseUrl + restEndpointPrice)
                .queryParam("date", date.toString())
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId).toUriString();

        ResponseEntity<PriceResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(null, buildHeaders()), PriceResponse.class);
        PriceResponse priceResponse = responseEntity.getBody();

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertThat(priceListResult).isEqualByComparingTo(priceResponse.priceList());
        assertThat(priceResult).isEqualByComparingTo(priceResponse.price());
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static Stream<Arguments> priceInputs() {
        return Stream.of(
                arguments(LocalDateTime.of(2020, 6, 14, 10, 0, 0), 1, BigDecimal.valueOf(35.5)),
                arguments(LocalDateTime.of(2020, 6, 14, 16, 0, 0), 2, BigDecimal.valueOf(25.45)),
                arguments(LocalDateTime.of(2020, 6, 14, 21, 0, 0), 1, BigDecimal.valueOf(35.5)),
                arguments(LocalDateTime.of(2020, 6, 15, 10, 0, 0), 3, BigDecimal.valueOf(30.5)),
                arguments(LocalDateTime.of(2020, 6, 16, 21, 0, 0), 4, BigDecimal.valueOf(38.95))
        );
    }
}
