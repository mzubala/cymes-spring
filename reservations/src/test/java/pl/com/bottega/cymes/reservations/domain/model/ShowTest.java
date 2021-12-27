package pl.com.bottega.cymes.reservations.domain.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.cymes.reservations.domain.model.CinemaHallExample.aCinemaHall;
import static pl.com.bottega.cymes.reservations.domain.model.PriceMapExample.aPriceMap;

public class ShowTest {
    @Test
    public void cannotCreateShowWithoutId() {
        assertThatThrownBy(() -> {
            new Show(null, new HashMap<>(), aCinemaHall().build());
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void cannotCreateShowWithoutPrices() {
        assertThatThrownBy(() -> {
            new Show(UUID.randomUUID(), null, aCinemaHall().build());
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void cannotCreateShowWithoutCinemaHall() {
        assertThatThrownBy(() -> {
            new Show(UUID.randomUUID(), aPriceMap().withTicketKinds("regular").build(), null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void cannotCreateShowWithoutAnyPrices() {
        assertThatThrownBy(() -> {
            new Show(UUID.randomUUID(), new HashMap<>(), aCinemaHall().build());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
