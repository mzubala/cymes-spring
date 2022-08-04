package pl.com.bottega.cymes.reservations.domain.model.model.application;

import lombok.AllArgsConstructor;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Reservation;

@AllArgsConstructor
public class SelectShowHandler {

    private final ShowProvider showProvider;
    private final ReservationRepository reservationRepository;

    public void handle(SelectShowCommand command) {
        var reservation = new Reservation(command.getReservationId());
        var show = showProvider.get(command.getShowId());
        reservation.selectShow(show);
        reservationRepository.save(reservation);
    }

}
