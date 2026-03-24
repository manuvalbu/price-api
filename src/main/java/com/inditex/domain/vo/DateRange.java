package com.inditex.domain.vo;

import java.time.LocalDateTime;
import java.util.Objects;

public record DateRange(
        LocalDateTime start,
        LocalDateTime end
) {

    public DateRange {
        Objects.requireNonNull(start, "Start date cannot be null");
        Objects.requireNonNull(end, "End date cannot be null");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    public static DateRange of(LocalDateTime start, LocalDateTime end) {
        return new DateRange(start, end);
    }

    public boolean contains(LocalDateTime date) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
