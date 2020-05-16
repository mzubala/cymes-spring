package pl.com.bottega.cymes.showscheduler.domain;

import java.util.UUID;

public interface ShowRepository {
    void save(Show show);

    boolean anyShowsCollidingWith(Show show);

    Show get(UUID showId);
}
