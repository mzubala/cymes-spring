package pl.com.bottega.cymes.reservations.domain.application;

import lombok.AllArgsConstructor;
import pl.com.bottega.cymes.reservations.domain.model.Seat;
import pl.com.bottega.cymes.reservations.domain.model.SeatReservation;

@AllArgsConstructor
public class SelectSeatsHandler {

    private final ReservationRepository reservationRepository;
    private SeatReservationRepository seatReservationRepository;

    public void handle(SelectSeatsCommand command) {
        var reservation = reservationRepository.get(command.getReservationId());
        reservation.selectSeats(command.getSeats());
        command.getSeats().forEach((seat) -> {
            var seatReservation = new SeatReservation(reservation, seat);
            seatReservationRepository.save(seatReservation);
        });
        reservationRepository.save(reservation);
    }

}
