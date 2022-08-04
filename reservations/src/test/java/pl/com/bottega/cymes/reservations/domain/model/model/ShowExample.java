package pl.com.bottega.cymes.reservations.domain.model.model;

import pl.com.bottega.cymes.reservations.domain.model.model.model.Money;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Show;
import pl.com.bottega.cymes.reservations.domain.model.model.model.TicketKind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ShowExample {

    private UUID id = UUID.randomUUID();
    private Map<TicketKind, Money> prices = new HashMap<>();

    static Show anyShow() {
        return new ShowExample().toDomain();
    }

    static ShowExample aShow() {
        return new ShowExample();
    }

    private Show toDomain() {
        return new Show(id, prices);
    }

    ShowExample withAvailableTicketKinds(String... kinds) {
        this.prices = Arrays.stream(kinds)
                .map(TicketKind::new)
                .reduce(new HashMap<>(), (acc, kind) -> {
                    acc.put(kind, MoneyExample.TEN);
                    return acc;
                }, (acc1, acc2) -> {
                    acc1.putAll(acc2);
                    return acc1;
                });
        return this;
    }

    ShowExample withAvailableTicketKind(String kind, Money price) {
        prices.put(new TicketKind(kind), price);
        return this;
    }

    Show build() {
        return new Show(id, prices);
    }
}
