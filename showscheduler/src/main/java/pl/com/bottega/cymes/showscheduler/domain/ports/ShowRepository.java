package pl.com.bottega.cymes.showscheduler.domain.ports;

import pl.com.bottega.cymes.showscheduler.domain.Show;

import java.util.UUID;

public interface ShowRepository {
    void save(Show show);

    boolean anyShowsCollidingWith(Show show);

    Show get(UUID showId);

    class ShowNotFoundException extends RuntimeException {}
}
