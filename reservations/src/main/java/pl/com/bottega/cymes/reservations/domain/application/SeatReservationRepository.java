package pl.com.bottega.cymes.reservations.domain.application;

import pl.com.bottega.cymes.reservations.domain.model.SeatReservation;

public interface SeatReservationRepository {
    void save(SeatReservation seatReservation);
}
