package com.inditex.ut.domain;

import com.inditex.domain.entity.Price;
import com.inditex.domain.vo.Currency;
import com.inditex.domain.vo.DateRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class PriceTest {

    @Nested
    @DisplayName("Currency test cases")
    class CurrencyTestCases {
        @ParameterizedTest(name = "Null currency should fail")
        @CsvSource(value = {"1,1,1,1,1.1,NULL,2020-06-14T00:00:00,2020-12-31T23:59:59"},
                nullValues = "NULL")
        void null_currency_checks(Long productId,
                                  Long brandId,
                                  Integer priceList,
                                  Integer priority,
                                  BigDecimal price,
                                  String currency,
                                  LocalDateTime start,
                                  LocalDateTime end) {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.builder().productId(productId).brandId(brandId).priceList(priceList).priority(priority).price(price)
                            .currency(Currency.of(currency))
                            .dateRange(DateRange.of(start,end))
                            .build());
        }

        @ParameterizedTest(name = "Wrong currency should fail")
        @CsvSource(value = {"1,1,1,1,1.1,AA,2020-06-14T00:00:00,2020-12-31T23:59:59"},
                nullValues = "NULL")
        void wrong_currency_checks(Long productId,
                                  Long brandId,
                                  Integer priceList,
                                  Integer priority,
                                  BigDecimal price,
                                  String currency,
                                  LocalDateTime start,
                                  LocalDateTime end)
         {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Price.builder().productId(productId).brandId(brandId).priceList(priceList).priority(priority).price(price)
                            .currency(Currency.of(currency))
                            .dateRange(DateRange.of(start,end))
                            .build());
        }
    }

    @Nested
    @DisplayName("DateRange test cases")
    class DateRangeTestCases {
        @ParameterizedTest(name = "Null start date should fail")
        @CsvSource(value = {"1,1,1,1,1.1,EUR,NULL,2020-12-31T23:59:59"},
                nullValues = "NULL")
        void null_start_date_checks(Long productId,
                                    Long brandId,
                                    Integer priceList,
                                    Integer priority,
                                    BigDecimal price,
                                    String currency,
                                    LocalDateTime start,
                                    LocalDateTime end) {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.builder().productId(productId).brandId(brandId).priceList(priceList).priority(priority).price(price)
                            .currency(Currency.of(currency))
                            .dateRange(DateRange.of(start,end))
                            .build());
        }

        @ParameterizedTest(name = "Null end date should fail")
        @CsvSource(value = {"1,1,1,1,1.1,EUR,2020-06-14T00:00:00,NULL"},
                nullValues = "NULL")
        void null_end_date_checks(Long productId,
                                    Long brandId,
                                    Integer priceList,
                                    Integer priority,
                                    BigDecimal price,
                                    String currency,
                                    LocalDateTime start,
                                    LocalDateTime end) {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.builder().productId(productId).brandId(brandId).priceList(priceList).priority(priority).price(price)
                            .currency(Currency.of(currency))
                            .dateRange(DateRange.of(start,end))
                            .build());
        }

        @ParameterizedTest(name = "Wrong date should fail")
        @CsvSource(value = {"1,1,1,1,1.1,EUR,2020-06-14T00:00:00,2020-06-13T23:59:59"},
                nullValues = "NULL")
        void wrong_date_checks(Long productId,
                                  Long brandId,
                                  Integer priceList,
                                  Integer priority,
                                  BigDecimal price,
                                  String currency,
                                  LocalDateTime start,
                                  LocalDateTime end) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Price.builder().productId(productId).brandId(brandId).priceList(priceList).priority(priority).price(price)
                            .currency(Currency.of(currency))
                            .dateRange(DateRange.of(start,end))
                            .build());
        }
    }
}
