package com.inditex.ut.domain;

import com.inditex.domain.entity.Price;
import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("Price domain tests")
class PriceTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 12, 31, 23, 59);

    private Price.PriceBuilder validBuilder() {
        return Price.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(35.50))
                .currency(Currency.of("EUR"))
                .dateRange(DateRange.of(START, END));
    }

    // Happy path

    @Nested
    @DisplayName("Valid Price creation")
    class ValidPriceCreation {

        @Test
        @DisplayName("Should create a valid Price successfully")
        void should_create_valid_price() {
            Price price = validBuilder().build();

            assertThat(price.productId()).isEqualTo(35455L);
            assertThat(price.brandId()).isEqualTo(1L);
            assertThat(price.priceList()).isEqualTo(1);
            assertThat(price.priority()).isZero();
            assertThat(price.price()).isEqualByComparingTo("35.50");
            assertThat(price.currency().code()).isEqualTo("EUR");
            assertThat(price.dateRange().start()).isEqualTo(START);
            assertThat(price.dateRange().end()).isEqualTo(END);
        }

        @Test
        @DisplayName("priority = 0 should be accepted")
        void zero_priority_should_be_accepted() {
            Price price = validBuilder().priority(0).build();
            assertThat(price.priority()).isZero();
        }

        @Test
        @DisplayName("price = 0 should be accepted")
        void zero_price_should_be_accepted() {
            Price price = validBuilder().price(BigDecimal.ZERO).build();
            assertThat(price.price()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    // Price field validations (null)

    @Nested
    @DisplayName("Price null field validations")
    class PriceNullValidations {

        @Test
        @DisplayName("Null productId should fail")
        void null_productId_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().productId(null).build())
                    .withMessageContaining("productId");
        }

        @Test
        @DisplayName("Null brandId should fail")
        void null_brandId_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().brandId(null).build())
                    .withMessageContaining("brandId");
        }

        @Test
        @DisplayName("Null priceList should fail")
        void null_priceList_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().priceList(null).build())
                    .withMessageContaining("priceList");
        }

        @Test
        @DisplayName("Null priority should fail")
        void null_priority_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().priority(null).build())
                    .withMessageContaining("priority");
        }

        @Test
        @DisplayName("Null price should fail")
        void null_price_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().price(null).build())
                    .withMessageContaining("price");
        }

        @Test
        @DisplayName("Null currency should fail")
        void null_currency_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().currency(null).build())
                    .withMessageContaining("currency");
        }

        @Test
        @DisplayName("Null dateRange should fail")
        void null_dateRange_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> validBuilder().dateRange(null).build())
                    .withMessageContaining("dateRange");
        }
    }

    // Price field validations (business rules)

    @Nested
    @DisplayName("Price business rule validations")
    class PriceBusinessRuleValidations {

        @ParameterizedTest(name = "productId = {0} should fail")
        @ValueSource(longs = {0L, -1L, -100L})
        void non_positive_productId_should_fail(long productId) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> validBuilder().productId(productId).build())
                    .withMessageContaining("productId");
        }

        @ParameterizedTest(name = "brandId = {0} should fail")
        @ValueSource(longs = {0L, -1L, -50L})
        void non_positive_brandId_should_fail(long brandId) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> validBuilder().brandId(brandId).build())
                    .withMessageContaining("brandId");
        }

        @ParameterizedTest(name = "priceList = {0} should fail")
        @ValueSource(ints = {0, -1, -10})
        void non_positive_priceList_should_fail(int priceList) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> validBuilder().priceList(priceList).build())
                    .withMessageContaining("priceList");
        }

        @ParameterizedTest(name = "priority = {0} should fail")
        @ValueSource(ints = {-1, -5, -100})
        void negative_priority_should_fail(int priority) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> validBuilder().priority(priority).build())
                    .withMessageContaining("priority");
        }

        @ParameterizedTest(name = "price = {0} should fail")
        @ValueSource(strings = {"-0.01", "-1", "-10.50"})
        void negative_price_should_fail(String priceValue) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> validBuilder().price(new BigDecimal(priceValue)).build())
                    .withMessageContaining("price");
        }
    }

    // Currency value object

    @Nested
    @DisplayName("Currency value object")
    class CurrencyTestCases {

        @Test
        @DisplayName("Valid ISO-4217 code should be accepted")
        void valid_currency_should_be_accepted() {
            Currency currency = Currency.of("EUR");
            assertThat(currency.code()).isEqualTo("EUR");
        }

        @Test
        @DisplayName("Null currency code should fail")
        void null_currency_code_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Currency.of(null))
                    .withMessageContaining("Currency");
        }

        @ParameterizedTest(name = "Invalid currency code \"{0}\" should fail")
        @ValueSource(strings = {"", "A", "AA", "EURO", "EU", "1234"})
        void invalid_currency_code_should_fail(String code) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Currency.of(code))
                    .withMessageContaining("ISO-4217");
        }
    }

    // DateRange value object

    @Nested
    @DisplayName("DateRange value object")
    class DateRangeTestCases {

        @Test
        @DisplayName("Valid date range should be accepted")
        void valid_date_range_should_be_accepted() {
            DateRange range = DateRange.of(START, END);
            assertThat(range.start()).isEqualTo(START);
            assertThat(range.end()).isEqualTo(END);
        }

        @Test
        @DisplayName("Null start date should fail")
        void null_start_date_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> DateRange.of(null, END))
                    .withMessageContaining("Start");
        }

        @Test
        @DisplayName("Null end date should fail")
        void null_end_date_should_fail() {
            assertThatNullPointerException()
                    .isThrownBy(() -> DateRange.of(START, null))
                    .withMessageContaining("End");
        }

        @Test
        @DisplayName("End date before start date should fail")
        void end_before_start_should_fail() {
            LocalDateTime earlier = START.minusDays(1);
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> DateRange.of(START, earlier))
                    .withMessageContaining("End date");
        }

        @Test
        @DisplayName("Start equal to end should be accepted")
        void start_equal_end_should_be_accepted() {
            DateRange range = DateRange.of(START, START);
            assertThat(range.start()).isEqualTo(range.end());
        }
    }

    // isApplicable behaviour

    @Nested
    @DisplayName("isApplicable behaviour")
    class IsApplicableTests {

        @Test
        @DisplayName("Date inside range should be applicable")
        void date_inside_range_should_be_applicable() {
            Price price = validBuilder().build();
            LocalDateTime inside = LocalDateTime.of(2020, 8, 15, 12, 0);

            assertThat(price.isApplicable(inside)).isTrue();
        }

        @Test
        @DisplayName("Date equal to start should be applicable (inclusive)")
        void date_equal_start_should_be_applicable() {
            Price price = validBuilder().build();
            assertThat(price.isApplicable(START)).isTrue();
        }

        @Test
        @DisplayName("Date equal to end should be applicable (inclusive)")
        void date_equal_end_should_be_applicable() {
            Price price = validBuilder().build();
            assertThat(price.isApplicable(END)).isTrue();
        }

        @Test
        @DisplayName("Date before start should not be applicable")
        void date_before_start_should_not_be_applicable() {
            Price price = validBuilder().build();
            assertThat(price.isApplicable(START.minusSeconds(1))).isFalse();
        }

        @Test
        @DisplayName("Date after end should not be applicable")
        void date_after_end_should_not_be_applicable() {
            Price price = validBuilder().build();
            assertThat(price.isApplicable(END.plusSeconds(1))).isFalse();
        }
    }
}