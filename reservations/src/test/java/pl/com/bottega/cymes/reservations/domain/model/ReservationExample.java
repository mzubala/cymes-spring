package pl.com.bottega.cymes.reservations.domain.model;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.com.bottega.cymes.reservations.domain.model.ShowExample.aShow;
import static pl.com.bottega.cymes.reservations.domain.model.TicketsMapExample.aTicketsMap;

class ReservationExample {

    private UUID id = UUID.randomUUID();

    private ShowExample showToSelect;
    private TicketsMapExample ticketsToSelect;
    private Set<Seat> seatsToSelect;
    private PaymentMethod paymentMethodToSelect;

    static Reservation newReservation() {
        return new ReservationExample().build();
    }

    static ReservationExample aReservation() {
        return new ReservationExample();
    }

    Reservation build() {
        var reservation = new Reservation(id);
        if(showToSelect != null) {
            reservation.selectShow(showToSelect.build());
        }
        if(ticketsToSelect != null) {
            reservation.selectTickets(ticketsToSelect.build());
        }
        if(seatsToSelect != null) {
            reservation.selectSeats(seatsToSelect);
        }
        if(paymentMethodToSelect != null) {
            reservation.selectPaymentMethod(paymentMethodToSelect);
        }
        return reservation;
    }

    ReservationExample withSelectedShow(ShowExample show) {
        this.showToSelect = show;
        return this;
    }

    public ReservationExample withSelectedTickets(TicketsMapExample ticketsMap) {
        this.ticketsToSelect = ticketsMap;
        return this;
    }

    public ReservationExample withSelectedTickets() {
        var example = this;
        if(showToSelect == null) {
            example = withSelectedShow(aShow());
        }
        var show = example.showToSelect.build();
        var ticketKind = show.getPrices().keySet().stream().findFirst().get();
        return withSelectedTickets(aTicketsMap().withTicketsCount(ticketKind.getName(), 2));
    }

    public ReservationExample withSelectedSeats(Set<Seat> seats) {
        this.seatsToSelect = seats;
        return this;
    }

    public ReservationExample withSelectedSeats() {
        var example = this;
        if(ticketsToSelect == null) {
            example = withSelectedTickets();
        }
        var show = example.showToSelect.build();
        var seats = new LinkedList<>(show.getCinemaHall().getSeats());
        var ticketsMap = example.ticketsToSelect.build();
        var countOfSeats = ticketsMap.values().stream().mapToInt(i -> i).sum();
        return example.withSelectedSeats(IntStream.range(0, countOfSeats).mapToObj(seats::get).collect(Collectors.toSet()));
    }

    public ReservationExample withSelectedPaymentMethod(PaymentMethod method) {
        var example = this;
        if(example.seatsToSelect == null) {
            example = withSelectedSeats();
        }
        example.paymentMethodToSelect = method;
        return example;
    }

    public ReservationExample withSelectedPaymentMethod() {
        return withSelectedPaymentMethod(PaymentMethod.ONLINE);
    }
}
