package pl.com.bottega.cymes.reservations.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
public class CinemaHall {
    @NonNull
    Set<Seat> seats;

    public void validateHasSeats(Set<Seat> seats) {
        if(!this.seats.containsAll(seats)) {
            throw new InvalidReservationOperationException("Invalid seats numbers provided");
        }
    }
}
