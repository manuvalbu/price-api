package com.inditex.ut.domain;

import com.inditex.domain.entity.Price;
import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.domain.service.PriceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PriceResolverTest {

    private PriceResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new PriceResolver();
    }

    // ────────────────────────────────────────────────
    //  Constants
    // ────────────────────────────────────────────────
    private static final Long PRODUCT_35455 = 35455L;
    private static final Long BRAND_ZARA = 1L;

    // Official sample data from the exercise – using the current Price.of signature
    private List<Price> samplePrices() {
        return List.of(
                Price.of(PRODUCT_35455, BRAND_ZARA, 1, 0, "35.50", "EUR", "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
                Price.of(PRODUCT_35455, BRAND_ZARA, 2, 1, "25.45", "EUR", "2020-06-14T15:00:00", "2020-06-14T18:30:00"),
                Price.of(PRODUCT_35455, BRAND_ZARA, 3, 1, "30.50", "EUR", "2020-06-15T00:00:00", "2020-06-15T11:00:00"),
                Price.of(PRODUCT_35455, BRAND_ZARA, 4, 1, "38.95", "EUR", "2020-06-15T16:00:00", "2020-12-31T23:59:59")
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
                    Price.of(PRODUCT_35455, BRAND_ZARA, 1, 0, "45.00", "EUR", "2020-06-14T00:00:00", "2020-06-14T23:59:59"),
                    Price.of(PRODUCT_35455, BRAND_ZARA, 8, 5, "55.00", "EUR", "2020-06-14T00:00:00", "2020-06-14T23:59:59"),
                    Price.of(PRODUCT_35455, BRAND_ZARA, 9, 10, "22.00", "EUR", "2020-06-14T12:00:00", "2020-06-14T18:00:00")
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