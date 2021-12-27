package pl.com.bottega.cymes.reservations.domain.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ShowExample {

    private UUID id = UUID.randomUUID();
    private PriceMapExample priceMapExample = new PriceMapExample();
    private CinemaHallExample cinemaHallExample = new CinemaHallExample();

    static Show anyShow() {
        return new ShowExample().build();
    }

    static ShowExample aShow() {
        return new ShowExample();
    }

    ShowExample withAvailableTicketKinds(String... kinds) {
        priceMapExample = priceMapExample.withTicketKinds(kinds);
        return this;
    }

    ShowExample withAvailableTicketKind(String kind, Money price) {
        priceMapExample = priceMapExample.withTicketKind(kind, price);
        return this;
    }

    ShowExample inCinemaHall(CinemaHallExample cinemaHall) {
        this.cinemaHallExample = cinemaHall;
        return this;
    }

    Show build() {
        return new Show(id, priceMapExample.build(), cinemaHallExample.build());
    }
}
