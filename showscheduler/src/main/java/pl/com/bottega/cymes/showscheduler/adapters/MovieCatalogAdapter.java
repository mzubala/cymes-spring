package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;

@Component
@RequiredArgsConstructor
public class MovieCatalogAdapter implements MovieCatalog {

    private final WebClient moviesClient;

    @Override
    public Movie get(Long movieId) {
        return null;
    }
}
