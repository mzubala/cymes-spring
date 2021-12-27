package pl.com.bottega.cymes.reservations.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.cymes.reservations.domain.model.CinemaHallExample.aCinemaHall;
import static pl.com.bottega.cymes.reservations.domain.model.CustomerExample.aRegisteredCustomer;
import static pl.com.bottega.cymes.reservations.domain.model.ReceiptExample.aReceipt;
import static pl.com.bottega.cymes.reservations.domain.model.ReservationExample.aReservation;
import static pl.com.bottega.cymes.reservations.domain.model.ReservationExample.newReservation;
import static pl.com.bottega.cymes.reservations.domain.model.ShowExample.aShow;
import static pl.com.bottega.cymes.reservations.domain.model.ShowExample.anyShow;
import static pl.com.bottega.cymes.reservations.domain.model.TicketsMapExample.aTicketsMap;

public class ReservationTest {

    @Test
    public void selectsShow() {
        // given
        var reservation = newReservation();
        var show = anyShow();

        // when
        reservation.selectShow(show);

        // then
        assertThat(reservation.getSelectedShow()).contains(show);
    }

    @Test
    public void cannotSelectAShowTwice() {
        // given
        var reservation = newReservation();
        var show = anyShow();
        var otherShow = anyShow();

        // when
        reservation.selectShow(show);

        // then
        assertThatThrownBy(() -> reservation.selectShow(otherShow)).isInstanceOf(InvalidReservationOperationException.class);
        assertThat(reservation.getSelectedShow()).contains(show);
    }

    @Test
    public void cannotSelectANullShow() {
        // expect
        assertThatThrownBy(() -> newReservation().selectShow(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void selectsTickets() {
        // given
        var show = aShow().withAvailableTicketKinds("regular", "student", "senior");
        var reservation = aReservation().withSelectedShow(show).build();
        var ticketsMap = aTicketsMap().withTicketsCount("student", 2).build();

        // when
        reservation.selectTickets(ticketsMap);

        // then
        assertThat(reservation.getSelectedTickets()).isEqualTo(ticketsMap);
    }

    @Test
    public void cannotSelectTicketsThatAreNotAvailable() {
        // given
        var show = aShow().withAvailableTicketKinds("regular", "student", "senior");
        var reservation = aReservation().withSelectedShow(show).build();
        var ticketsMap = aTicketsMap()
            .withTicketsCount("regular", 2)
            .withTicketsCount("school", 1)
            .build();

        // expect
        assertThatThrownBy(() -> reservation.selectTickets(ticketsMap))
            .isInstanceOf(InvalidReservationOperationException.class);
    }

    @Test
    public void selectSeats() {
        // given
        var show = aShow().withAvailableTicketKinds("regular", "senior")
            .inCinemaHall(aCinemaHall().withSeatsInRow(1, 5));
        var reservation = aReservation().withSelectedShow(show)
            .withSelectedTickets(
                aTicketsMap().withTicketsCount("regular", 2).withTicketsCount("senior", 1)
            ).build();

        // when
        var seatsToSelect = Set.of(
            new Seat(1, 1),
            new Seat(1, 2),
            new Seat(1, 3)
        );
        reservation.selectSeats(seatsToSelect);

        // then
        assertThat(reservation.getSelectedSeats()).containsExactlyInAnyOrderElementsOf(seatsToSelect);
    }

    @Test
    public void changesSeatsSelection() {
        // given
        var show = aShow().withAvailableTicketKinds("regular")
            .inCinemaHall(aCinemaHall().withSeatsInRow(1, 5));
        var reservation = aReservation().withSelectedShow(show)
            .withSelectedTickets(
                aTicketsMap().withTicketsCount("regular", 1)
            ).build();

        // when
        reservation.selectSeats(Set.of(new Seat(1, 2)));
        var secondSelection = Set.of(new Seat(1, 1));
        reservation.selectSeats(secondSelection);

        // then
        assertThat(reservation.getSelectedSeats()).containsExactlyInAnyOrderElementsOf(secondSelection);
    }

    @Test
    public void enforcesSelectingCorrectNumberOfSeats() {
        var show = aShow().withAvailableTicketKinds("regular", "student", "senior");
        var reservation = aReservation()
            .withSelectedShow(show)
            .withSelectedTickets(aTicketsMap().withTicketsCount("regular", 2))
            .build();

        // expect
        assertThatThrownBy(() -> reservation.selectSeats(Set.of(new Seat(1, 1))))
            .isInstanceOf(InvalidReservationOperationException.class);
        assertThatThrownBy(() -> reservation.selectSeats(Set.of(
            new Seat(1, 1),
            new Seat(1, 2),
            new Seat(1, 3)))
        ).isInstanceOf(InvalidReservationOperationException.class);
    }

    @Test
    public void returnsReceipt() {
        // given
        var show = aShow()
            .withAvailableTicketKind("regular", Money.of(59.99))
            .withAvailableTicketKind("school", Money.of(29.99));
        var reservation = aReservation().withSelectedShow(show).build();
        var ticketsMap = aTicketsMap()
            .withTicketsCount("regular", 2)
            .withTicketsCount("school", 1)
            .build();

        // when
        reservation.selectTickets(ticketsMap);

        // then
        assertThat(reservation.getReceipt()).isEqualTo(
            aReceipt()
                .withTicketKind("regular", 2, Money.of(59.99))
                .withTicketKind("school", 1, Money.of(29.99))
                .build()
        );
    }

    @Test
    public void doesNotAllowToSelectInvalidSeat() {
        // given
        var show = aShow().withAvailableTicketKinds("regular")
            .inCinemaHall(aCinemaHall().withSeatsInRow(1, 10));
        var ticketsMap = aTicketsMap()
            .withTicketsCount("regular", 2);
        var reservation = aReservation().withSelectedShow(show)
            .withSelectedTickets(ticketsMap)
            .build();


        // expect
        assertThatThrownBy(() -> reservation.selectSeats(Set.of(
            new Seat(1, 5),
            new Seat(1, 12)))
        ).isInstanceOf(InvalidReservationOperationException.class);
    }

    @Test
    public void unselectsSeatsWhenAgainSelectingTickets() {
        // given
        var show = aShow().withAvailableTicketKinds("regular", "student");
        var reservation = aReservation()
            .withSelectedShow(show)
            .withSelectedSeats().build();

        // when
        reservation.selectTickets(aTicketsMap().withTicketsCount("student", 10).build());

        // then
        assertThat(reservation.getSelectedSeats()).isEmpty();
    }

    @Test
    public void newReservationDoesNotHaveShowSelected() {
        assertThat(aReservation().build().getSelectedShow()).isEmpty();
    }

    @Test
    public void newReservationDoesNotHaveTicketsSelected() {
        assertThat(aReservation().build().getSelectedTickets()).isEmpty();
    }

    @Test
    public void newReservationDoesNotHaveSeatsSelected() {
        assertThat(aReservation().build().getSelectedSeats()).isEmpty();
    }

    @Test
    public void afterSelectingShowNoTicketsAreSelected() {
        assertThat(aReservation().withSelectedShow(aShow()).build().getSelectedTickets()).isEmpty();
    }

    @Test
    public void selectsPaymentMethod() {
        // given
        var reservation = aReservation().withSelectedSeats().build();

        // when
        reservation.selectPaymentMethod(PaymentMethod.ONLINE);

        // then
        assertThat(reservation.getPaymentMethod()).contains(PaymentMethod.ONLINE);
    }

    @Test
    public void reservationDoesNotHavePaymentMethodBeforeItsExplicitlySelected() {
        // expect
        assertThat(aReservation().build().getPaymentMethod()).isEmpty();
        assertThat(aReservation().withSelectedShow(aShow()).build().getPaymentMethod()).isEmpty();
        assertThat(aReservation().withSelectedTickets().build().getPaymentMethod()).isEmpty();
        assertThat(aReservation().withSelectedSeats().build().getPaymentMethod()).isEmpty();
    }

    @Test
    public void cannotChangePaymentMethodIfItWasAlreadySelected() {
        // given
        var reservation = aReservation().withSelectedPaymentMethod(PaymentMethod.ONLINE).build();

        // expect
        assertThatThrownBy(() -> reservation.selectPaymentMethod(PaymentMethod.OFFLINE))
            .isInstanceOf(InvalidReservationOperationException.class);
    }

    @Test
    public void selectingPaymentMethodTwiceHasNoEffect() {
        // given
        var reservation = aReservation().withSelectedPaymentMethod(PaymentMethod.ONLINE).build();

        // when
        reservation.selectPaymentMethod(PaymentMethod.ONLINE);

        // then
        assertThat(reservation.getPaymentMethod()).contains(PaymentMethod.ONLINE);
    }

    @Test
    public void setsCustomerData() {
        // given
        var reservation = aReservation().withSelectedPaymentMethod().build();
        var customer = aRegisteredCustomer();

        // when
        reservation.enterCustomerInformation(customer);

        // then
        assertThat(reservation.getCustomer()).contains(customer);
    }
}
