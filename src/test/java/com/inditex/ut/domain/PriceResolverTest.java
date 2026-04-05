package com.inditex.ut.domain;

import com.inditex.domain.entity.Price;
import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.domain.service.PriceResolver;
import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class PriceResolverTest {

    private PriceResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new PriceResolver();
    }

    private static final Long PRODUCT_35455 = 35455L;
    private static final Long BRAND_ZARA = 1L;

    private List<Price> samplePrices() {
        return List.of(
                Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(1).priority(0)
                        .price(BigDecimal.valueOf(35.50)).currency(Currency.of("EUR"))
                        .dateRange(DateRange.of(LocalDateTime.parse("2020-06-14T00:00:00"),LocalDateTime.parse("2020-12-31T23:59:59")))
                        .build(),
                Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(2).priority(1)
                        .price(BigDecimal.valueOf(25.45)).currency(Currency.of("EUR"))
                        .dateRange(DateRange.of(LocalDateTime.parse("2020-06-14T15:00:00"),LocalDateTime.parse("2020-06-14T18:30:00")))
                        .build(),
                Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(3).priority(1)
                        .price(BigDecimal.valueOf(30.50)).currency(Currency.of("EUR"))
                        .dateRange(DateRange.of(LocalDateTime.parse("2020-06-15T00:00:00"),LocalDateTime.parse("2020-06-15T11:00:00")))
                        .build(),
                Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(4).priority(1)
                        .price(BigDecimal.valueOf(38.95)).currency(Currency.of("EUR"))
                        .dateRange(DateRange.of(LocalDateTime.parse("2020-06-15T16:00:00"),LocalDateTime.parse("2020-12-31T23:59:59")))
                        .build()
        );
    }

    @Nested
    @DisplayName("Official Inditex test cases")
    class OfficialTestCases {

        @Test
        @DisplayName("Test 1 – 2020-06-14 10:00 → price list 1 (35.50)")
        void test_1_14jun_10am() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-14T10:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(1);
            assertThat(result.priority()).isZero();
            assertThat(result.price()).isEqualByComparingTo("35.50");
        }

        @Test
        @DisplayName("Test 2 – 2020-06-14 16:00 → price list 2 (25.45)")
        void test_2_14jun_16pm() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-14T16:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(2);
            assertThat(result.priority()).isEqualTo(1);
            assertThat(result.price()).isEqualByComparingTo("25.45");
        }

        @Test
        @DisplayName("Test 3 – 2020-06-14 21:00 → price list 1 (35.50)")
        void test_3_14jun_21pm() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-14T21:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(1);
            assertThat(result.price()).isEqualByComparingTo("35.50");
        }

        @Test
        @DisplayName("Test 4 – 2020-06-15 10:00 → price list 3 (30.50)")
        void test_4_15jun_10am() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-15T10:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(3);
            assertThat(result.price()).isEqualByComparingTo("30.50");
        }

        @Test
        @DisplayName("Test 5 – 2020-06-16 21:00 → price list 4 (38.95)")
        void test_5_16jun_21pm() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-16T21:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(4);
            assertThat(result.price()).isEqualByComparingTo("38.95");
        }
    }

    @Nested
    @DisplayName("Priority & overlapping rules")
    class PriorityRules {

        @Test
        @DisplayName("Higher priority wins when multiple rules apply")
        void should_select_highest_priority() {
            var prices = List.of(
                    Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(1).priority(0)
                            .price(BigDecimal.valueOf(45.00)).currency(Currency.of("EUR"))
                            .dateRange(DateRange.of(LocalDateTime.parse("2020-06-14T00:00:00"),LocalDateTime.parse("2020-06-14T23:59:59")))
                            .build(),
                    Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(8).priority(5)
                            .price(BigDecimal.valueOf(55.00)).currency(Currency.of("EUR"))
                            .dateRange(DateRange.of(LocalDateTime.parse("2020-06-14T00:00:00"),LocalDateTime.parse("2020-06-14T23:59:59")))
                            .build(),
                    Price.builder().productId(PRODUCT_35455).brandId(BRAND_ZARA).priceList(9).priority(10)
                            .price(BigDecimal.valueOf(22.00)).currency(Currency.of("EUR"))
                            .dateRange(DateRange.of(LocalDateTime.parse("2020-06-14T12:00:00"),LocalDateTime.parse("2020-06-14T18:00:00")))
                            .build()
            );

            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-14T15:30:00"),
                    prices
            );

            assertThat(result.priority()).isEqualTo(10);
            assertThat(result.priceList()).isEqualTo(9);
            assertThat(result.price()).isEqualByComparingTo("22.00");
        }
    }

    @Nested
    @DisplayName("Negative & boundary cases")
    class NegativeAndBoundaryCases {

        @Test
        @DisplayName("No applicable price → should throw PriceNotFoundException")
        void no_applicable_price_throws_exception() {
            assertThatExceptionOfType(PriceNotFoundException.class)
                    .isThrownBy(() -> resolver.resolve(
                            LocalDateTime.parse("2020-06-13T00:00:00"),
                            samplePrices()
                    ));
        }

        @Test
        @DisplayName("Boundary: exactly at start → should be applicable")
        void applicable_exactly_at_start() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-15T16:00:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(4);
        }

        @Test
        @DisplayName("Boundary: exactly at end → should be applicable")
        void applicable_exactly_at_end() {
            var result = resolver.resolve(
                    LocalDateTime.parse("2020-06-14T18:30:00"),
                    samplePrices()
            );

            assertThat(result.priceList()).isEqualTo(2);
        }

        @Test
        @DisplayName("Null parameters should fail fast")
        void null_checks() {
            assertThatNullPointerException()
                    .isThrownBy(() -> resolver.resolve(null, samplePrices()))
                    .withMessageContaining("Date");

            assertThatNullPointerException()
                    .isThrownBy(() -> resolver.resolve(LocalDateTime.now(), null))
                    .withMessageContaining("Price List");
        }
    }
}