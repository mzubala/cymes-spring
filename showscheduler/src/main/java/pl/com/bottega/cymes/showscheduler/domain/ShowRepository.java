package pl.com.bottega.cymes.showscheduler.domain;

public interface ShowRepository {
    void save(Show show);

    boolean anyShowsCollidingWith(Show show);
}
