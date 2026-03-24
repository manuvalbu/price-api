package com.inditex.ut.domain;

import com.inditex.domain.entity.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class PriceTest {

    @Nested
    @DisplayName("Currency test cases")
    class CurrencyTestCases {
        @Test
        @DisplayName("Null currency should fail")
        void null_currency_checks() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.of(1, 1, 1, 1, "1.1", null, "2020-06-14T00:00:00", "2020-12-31T23:59:59"));
        }

        @Test
        @DisplayName("Wrong currency should fail")
        void wrong_currency_checks() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Price.of(1, 1, 1, 1, "1.1", "ABCD", "2020-06-14T00:00:00", "2020-12-31T23:59:59"));
        }
    }

    @Nested
    @DisplayName("DateRange test cases")
    class DateRangeTestCases {
        @Test
        @DisplayName("Null start date should fail")
        void null_start_date_checks() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.of(1, 1, 1, 1, "1.1", "EUR", null, "2020-12-31T23:59:59"));
        }

        @Test
        @DisplayName("Null end date should fail")
        void null_end_date_checks() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Price.of(1, 1, 1, 1, "1.1", "EUR", "2020-06-14T00:00:00", null));
        }

        @Test
        @DisplayName("Wrong date should fail")
        void wrong_date_checks() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Price.of(1, 1, 1, 1, "1.1", "EUR", "2020-06-14T00:00:01", "2020-06-14T00:00:00"));
        }
    }
}
