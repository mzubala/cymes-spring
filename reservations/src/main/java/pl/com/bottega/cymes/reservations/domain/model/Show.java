package pl.com.bottega.cymes.reservations.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Value
public class Show {
    @NonNull
    UUID id;
    @NonNull
    Map<TicketKind, Money> prices;
    @NonNull
    CinemaHall cinemaHall;

    public Show(@NonNull UUID id, @NonNull Map<TicketKind, Money> prices, @NonNull CinemaHall cinemaHall) {
        this.id = id;
        this.prices = prices;
        this.cinemaHall = cinemaHall;
        if(prices.size() == 0) {
            throw new IllegalArgumentException("At least one price must be defined");
        }
    }

    public void validateTicketKindsAreAvailable(Set<TicketKind> ticketsMap) {
        if(!prices.keySet().containsAll(ticketsMap)) {
            throw new InvalidReservationOperationException("Tickets not available exception");
        }
    }

    public void validateCinemaHallHasSeats(Set<Seat> seats) {
        cinemaHall.validateHasSeats(seats);
    }

    public Money priceOf(TicketKind key) {
        var price = prices.get(key);
        if(price == null) {
            throw new InvalidReservationOperationException("Tickets not available exception");
        }
        return price;
    }
}
