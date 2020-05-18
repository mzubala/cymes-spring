package pl.com.bottega.cymes.showscheduler.adapters;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class MovieCatalogAdapter implements MovieCatalog {

    private MoviesClient moviesClient;

    @Inject
    private YMLShowSchedulerConfiguration configuration;

    @PostConstruct
    public void init() {
        moviesClient = new ResteasyClientBuilder().build().target(configuration.moviesUrl()).proxy(MoviesClient.class);
    }

    @Override
    public Movie get(Long movieId) {
        var json = moviesClient.getMovie(movieId);
        return new Movie(json.getId(), json.getDurationMinutes());
    }
}
