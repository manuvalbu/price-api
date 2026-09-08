package com.inditex.e2e;

import com.inditex.PriceApplication;
import com.inditex.application.dto.PriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
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

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false; // avoid test exception in 4xx/5xx
            }
        });
    }

    @ParameterizedTest(name = "date={0} → priceList={1}, price={2}")
    @MethodSource("priceInputs")
    @DisplayName("Should return the applicable price for official test cases")
    void retrievePriceOKE2ETest(LocalDateTime date, Integer priceListResult, BigDecimal priceResult) {

        final String uri = buildUri(date, productId, brandId);
        
        ResponseEntity<PriceResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(null, buildHeaders()), PriceResponse.class);
        PriceResponse priceResponse = responseEntity.getBody();

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertThat(priceListResult).isEqualByComparingTo(priceResponse.priceList());
        assertThat(priceResult).isEqualByComparingTo(priceResponse.price());
    }

    @Test
    @DisplayName("Should return 404 when no price is applicable for the given date")
    void retrievePriceNotFoundForDateE2ETest() {
        LocalDateTime dateWithoutPrice = LocalDateTime.of(2020, 6, 13, 10, 0, 0);
        assertNotFound(buildUri(dateWithoutPrice, productId, brandId));
    }

    @Test
    @DisplayName("Should return 404 when product does not exist")
    void retrievePriceNotFoundForUnknownProductE2ETest() {
        Long unknownProductId = 99999L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        assertNotFound(buildUri(date, unknownProductId, brandId));
    }

    @Test
    @DisplayName("Should return 404 when brand does not exist")
    void retrievePriceNotFoundForUnknownBrandE2ETest() {
        Long unknownBrandId = 99L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        assertNotFound(buildUri(date, productId, unknownBrandId));
    }

    private void assertNotFound(String uri) {
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(null, buildHeaders()), Map.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Map<?, ?> body = responseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("message")).isNotNull();
        assertThat(body.get("message").toString()).isNotBlank();
        assertThat(body.get("code")).isNotNull();
    }

    private String buildUri(LocalDateTime date, Long productId, Long brandId) {
        return UriComponentsBuilder
                .fromHttpUrl(BASE_URL + randomServerPort + restBaseUrl + restEndpointPrice)
                .queryParam("date", date.toString())
                .queryParam("product_id", productId)
                .queryParam("brand_id", brandId)
                .toUriString();
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
