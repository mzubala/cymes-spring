package pl.com.bottega.cymes.reservations.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Reservation {
    private final UUID reservationId;
    private Show show;
    private Map<TicketKind, Integer> ticketsMap = new HashMap<>();
    private Set<Seat> seats = new HashSet<>();
    private Payment payment;
    private Customer customer;

    public Reservation(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public void selectShow(Show show) {
        validateShowCanBeSelected(show);
        this.show = show;
    }

    public void selectTickets(Map<TicketKind, Integer> ticketsMap) {
        show.validateTicketKindsAreAvailable(ticketsMap.keySet());
        this.seats.clear();
        this.ticketsMap = Collections.unmodifiableMap(ticketsMap);
    }

    public void selectSeats(Set<Seat> seats) {
        validateAllSeatsSelected(seats);
        show.validateCinemaHallHasSeats(seats);
        this.seats = seats;
    }

    private void validateAllSeatsSelected(Set<Seat> seats) {
        if(seats.size() != numberOfTickets()) {
            throw new InvalidReservationOperationException("Invalid number of seats selected");
        }
    }

    private int numberOfTickets() {
        return ticketsMap.values().stream().mapToInt(i -> i).sum();
    }

    private void validateShowCanBeSelected(Show show) {
        checkNotNull(show);
        if(this.show != null) {
            throw new InvalidReservationOperationException("Show already selected");
        }
    }

    public Optional<Show> getSelectedShow() {
        return Optional.ofNullable(show);
    }

    public Map<TicketKind, Integer> getSelectedTickets() {
        return ticketsMap;
    }

    public Receipt getReceipt() {
        var receipt = new Receipt();
        for(var entry: ticketsMap.entrySet()) {
            receipt = receipt.withItem(entry.getKey(), entry.getValue(), show.priceOf(entry.getKey()));
        }
        return receipt;
    }

    public Set<Seat> getSelectedSeats() {
        return Collections.unmodifiableSet(seats);
    }

    public void selectPaymentMethod(PaymentMethod method) {
        if(payment != null && !payment.getMethod().equals(method)) {
            throw new InvalidReservationOperationException("Payment has already been selected");
        }
        payment = Payment.of(method);
    }

    public Optional<PaymentMethod> getPaymentMethod() {
        return Optional.ofNullable(payment).map(Payment::getMethod);
    }

    public void enterCustomerInformation(Customer customer) {
        this.customer = customer;
    }

    public Optional<Customer> getCustomer() {
        return Optional.ofNullable(customer);
    }
}

