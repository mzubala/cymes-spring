package pl.com.bottega.cymes.reservations.domain.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceMapExample {
    private final Map<TicketKind, Money> priceMap = new HashMap<>();

    static PriceMapExample aPriceMap() {
        return new PriceMapExample();
    }

    PriceMapExample withTicketKinds(String... ticketKinds) {
        for(String ticket : ticketKinds) {
            priceMap.put(new TicketKind(ticket), Money.of(BigDecimal.TEN));
        }
        return this;
    }

    Map<TicketKind, Money> build() {
        if(priceMap.size() == 0) {
            return withTicketKinds("regular").build();
        }
        return priceMap;
    }

    PriceMapExample withTicketKind(String kind, Money price) {
        priceMap.put(new TicketKind(kind), price);
        return this;
    }
}
