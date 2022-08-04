package pl.com.bottega.cymes.reservations.domain.model.model.application;

import lombok.Value;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Seat;

import java.util.Set;
import java.util.UUID;

@Value
public class SelectSeatsCommand {
    UUID reservationId;
    Set<Seat> seats;

}
