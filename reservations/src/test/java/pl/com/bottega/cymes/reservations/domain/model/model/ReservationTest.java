package pl.com.bottega.cymes.reservations.domain.model.model;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Money;
import pl.com.bottega.cymes.reservations.domain.model.model.model.ShowAlreadySelected;
import pl.com.bottega.cymes.reservations.domain.model.model.model.TicketsNotAvailableException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pl.com.bottega.cymes.reservations.domain.model.model.ReceiptExample.aReceipt;
import static pl.com.bottega.cymes.reservations.domain.model.model.ReservationExample.aReservation;
import static pl.com.bottega.cymes.reservations.domain.model.model.ReservationExample.newReservation;
import static pl.com.bottega.cymes.reservations.domain.model.model.ShowExample.aShow;
import static pl.com.bottega.cymes.reservations.domain.model.model.ShowExample.anyShow;
import static pl.com.bottega.cymes.reservations.domain.model.model.TicketsMapExample.aTicketsMap;

public class ReservationTest {

    @Test
    public void selectsShow() {
        // given
        var reservation = newReservation();
        var show = anyShow();

        // when
        reservation.selectShow(show);

        // then
        assertThat(reservation.getSelectedShow()).isEqualTo(show);
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
        assertThatThrownBy(() -> reservation.selectShow(otherShow)).isInstanceOf(ShowAlreadySelected.class);
        assertThat(reservation.getSelectedShow()).isEqualTo(show);
    }

    @Test
    public void cannotSelectANullShow() {
        // expect
        assertThatThrownBy(() -> newReservation().selectShow(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void selectsTickets() {
        // given
        var show = aShow().withAvailableTicketKinds("regular", "student", "senior").build();
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
        var show = aShow().withAvailableTicketKinds("regular", "student", "senior").build();
        var reservation = aReservation().withSelectedShow(show).build();
        var ticketsMap = aTicketsMap()
                .withTicketsCount("regular", 2)
                .withTicketsCount("school", 1)
                .build();

        // expect
        assertThatThrownBy(() -> reservation.selectTickets(ticketsMap))
                .isInstanceOf(TicketsNotAvailableException.class);
    }

    @Test
    public void returnsReceipt() {
        // given
        var show = aShow()
                .withAvailableTicketKind("regular", Money.of(59.99))
                .withAvailableTicketKind("school", Money.of(29.99))
                .build();
        var reservation = aReservation().withSelectedShow(show).build();
        var ticketsMap = aTicketsMap()
                .withTicketsCount("regular", 2)
                .withTicketsCount("school", 1)
                .build();

        // when
        reservation.selectTickets(ticketsMap);

        // then
        assertThat(reservation.getReceipt()).isEqualTo(
                aReceipt().withTicketKind("regular", 2, Money.of(59.99)).withTicketKind("school", 1, Money.of(29.99)).build()
        );
    }
}
