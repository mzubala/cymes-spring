package pl.com.bottega.cymes.reservations.domain.model;

import pl.com.bottega.cymes.reservations.domain.model.Money;

import java.math.BigDecimal;

class MoneyExample {
    static final Money ONE = Money.of(BigDecimal.ONE);
    static final Money TEN = Money.of(BigDecimal.TEN);
}
