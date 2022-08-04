package pl.com.bottega.cymes.reservations.domain.model.model;

import pl.com.bottega.cymes.reservations.domain.model.model.model.Reservation;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Show;

import java.util.UUID;

class ReservationExample {

    private UUID id = UUID.randomUUID();

    private Show showToSelect;

    static Reservation newReservation() {
        return new ReservationExample().build();
    }

    static ReservationExample aReservation() {
        return new ReservationExample();
    }

    Reservation build() {
        var reservation = new Reservation(id);
        if(showToSelect != null) {
            reservation.selectShow(showToSelect);
        }
        return reservation;
    }

    ReservationExample withSelectedShow(Show show) {
        this.showToSelect = show;
        return this;
    }
}
