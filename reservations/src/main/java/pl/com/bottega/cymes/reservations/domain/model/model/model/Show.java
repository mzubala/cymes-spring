package pl.com.bottega.cymes.reservations.domain.model.model.model;

import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Value
public class Show {
    UUID id;
    Map<TicketKind, Money> prices;

    public void validateTicketKindsAreAvailable(Set<TicketKind> ticketsMap) {
        if(!prices.keySet().containsAll(ticketsMap)) {
            throw new TicketsNotAvailableException();
        }
    }

    public void validateCinemaHallHasSeats(Set<Seat> seats) {

    }
}
