package pl.com.bottega.cymes.reservations.domain.model.model.application;

import pl.com.bottega.cymes.reservations.domain.model.model.model.Reservation;

import java.util.UUID;

public interface ReservationRepository {
    void save(Reservation reservation);

    Reservation get(UUID reservationId);
}
