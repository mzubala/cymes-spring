package pl.com.bottega.cymes.reservations.domain.model.model;

import pl.com.bottega.cymes.reservations.domain.model.model.model.TicketKind;

import java.util.HashMap;
import java.util.Map;

class TicketsMapExample {
    private Map<TicketKind, Integer> tickets = new HashMap<>();

    static TicketsMapExample aTicketsMap() {
        return new TicketsMapExample();
    }

    public TicketsMapExample withTicketsCount(String kind, int count) {
        tickets.put(new TicketKind(kind), count);
        return this;
    }

    public Map<TicketKind, Integer> build() {
        return tickets;
    }
}
