package pl.com.bottega.cymes.reservations.domain.application;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SelectTicketsHandler {

    private final ReservationRepository reservationRepository;

    public void handle(SelectTicketsCommand cmd) {
        var reservation = reservationRepository.get(cmd.getReservationId());
        reservation.selectTickets(cmd.getTicketsMap());
        reservationRepository.save(reservation);
    }
}
