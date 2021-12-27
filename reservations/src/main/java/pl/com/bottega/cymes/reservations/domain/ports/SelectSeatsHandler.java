package pl.com.bottega.cymes.reservations.domain.ports;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SelectSeatsHandler {

    private final ReservationRepository reservationRepository;

    public void handle(SelectSeatsCommand command) {
        var reservation = reservationRepository.get(command.getReservationId());
        reservation.selectSeats(command.getSeats());
        reservationRepository.save(reservation);
    }

}
