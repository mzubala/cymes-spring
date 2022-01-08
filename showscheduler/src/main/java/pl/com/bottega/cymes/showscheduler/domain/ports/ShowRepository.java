package pl.com.bottega.cymes.showscheduler.domain.ports;

import pl.com.bottega.cymes.showscheduler.domain.Show;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface ShowRepository {
    void save(Show show);

    boolean anyShowsCollidingWith(Show show);

    Show get(UUID showId);

    Stream<Show> findShowsBetweenInCinema(Instant from, Instant until, Long cinemaId);

    Stream<Show> findShowsBetweenInCinemaHall(Instant from, Instant until, Long cinemaHallId);

    class ShowNotFoundException extends RuntimeException {}
}
