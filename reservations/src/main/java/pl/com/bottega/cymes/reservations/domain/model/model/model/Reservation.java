package pl.com.bottega.cymes.reservations.domain.model.model.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Reservation {
    private final UUID reservationId;
    private Show show;
    private Map<TicketKind, Integer> ticketsMap;
    private Set<Seat> seats;
    private Receipt receipt;

    public Reservation(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public void selectShow(Show show) {
        validateShowCanBeSelected(show);
        this.show = show;
    }

    public void selectTickets(Map<TicketKind, Integer> ticketsMap) {
        show.validateTicketKindsAreAvailable(ticketsMap.keySet());
        this.ticketsMap = Collections.unmodifiableMap(ticketsMap);
    }

    public void selectSeats(Set<Seat> seats) {
        validateAllSeatsSelected(seats);
        show.validateCinemaHallHasSeats(seats);
        this.seats = seats;
    }

    private void validateAllSeatsSelected(Set<Seat> seats) {

    }

    private void validateShowCanBeSelected(Show show) {
        checkNotNull(show);
        if(this.show != null) {
            throw new ShowAlreadySelected();
        }
    }

    public Show getSelectedShow() {
        return show;
    }

    public Map<TicketKind, Integer> getSelectedTickets() {
        return ticketsMap;
    }

    public Receipt getReceipt() {
        return null;
    }
}

