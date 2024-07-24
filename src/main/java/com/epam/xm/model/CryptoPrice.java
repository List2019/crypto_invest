package com.epam.xm.model;

import static com.epam.xm.support.Constant.UTC;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public record CryptoPrice(
        Instant timestamp,
        String cryptoName,
        Double price
) {
    public LocalDate getLocalDate() {
        return timestamp().atZone(ZoneId.of(UTC)).toLocalDate();
    }
}
