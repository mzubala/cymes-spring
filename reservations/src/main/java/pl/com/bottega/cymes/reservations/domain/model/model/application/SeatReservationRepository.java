package pl.com.bottega.cymes.reservations.domain.model.model.application;

import pl.com.bottega.cymes.reservations.domain.model.model.model.SeatReservation;

public interface SeatReservationRepository {
    void save(SeatReservation seatReservation);
}
