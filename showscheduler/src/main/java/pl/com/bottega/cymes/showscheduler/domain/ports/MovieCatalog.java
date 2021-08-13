package pl.com.bottega.cymes.showscheduler.domain.ports;

import pl.com.bottega.cymes.showscheduler.domain.Movie;

public interface MovieCatalog {
    Movie get(Long movieId);
}
