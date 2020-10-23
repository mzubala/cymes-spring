package pl.com.bottega.cymes.reservations.domain.model;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Reservation {
    private final UUID reservationId;
    private Show show;
    private Map<TicketKind, Integer> ticketsMap;
    private Money total;
    private Set<Seat> seats;

    public Reservation(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public void selectShow(Show show) {
        validateShowCanBeSelected(show);
        this.show = show;
    }

    public void selectTickets(Map<TicketKind, Integer> ticketsMap) {
        show.validateTicketKindsAreAvailable(ticketsMap.keySet());
        this.ticketsMap = ticketsMap;
        this.total = show.calculateTicketsPrice(ticketsMap);
    }

    public void selectSeats(Set<Seat> seats) {
        validateAllSeatsSelected(seats);
        show.validateCinemaHallHasSeats(seats);
        this.seats = seats;
    }

    private void validateAllSeatsSelected(Set<Seat> seats) {

    }

    private void validateShowCanBeSelected(Show show) {

    }
}
