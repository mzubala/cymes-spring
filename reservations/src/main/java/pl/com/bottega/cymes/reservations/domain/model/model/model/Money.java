package pl.com.bottega.cymes.reservations.domain.model.model.model;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Value
public class Money {

    private static final Money ZERO = Money.of(0);

    BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return ZERO;
    }
}
