package pl.com.bottega.cymes.showscheduler.integration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.showscheduler.adapters.clients.MovieCatalogAdapter;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.lessThan;
import static com.github.tomakehurst.wiremock.client.WireMock.moreThan;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class MovieCatalogAdapterTest {

    @Autowired
    private MovieCatalogAdapter movieCatalogAdapter;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private MovieAbility movieAbility;

    @BeforeEach
    public void resetCircuitBreaker() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> cb.reset());
    }

    @Test
    public void returnsMovie() {
        // given
        var movie = new Movie(1L, 120);
        movieAbility.stubMovie(movie);

        // when
        var fetchedMovie = movieCatalogAdapter.get(movie.getId());

        // then
        assertThat(fetchedMovie).isEqualTo(movie);
    }

    @Test
    public void usesACircuitBreaker() {
        // given
        movieAbility.stubMoviesServiceFailure();
        int n = 200;

        // when
        for(int i = 0; i<n; i++) {
            try {
                movieCatalogAdapter.get((long) i);
            } catch (Exception ex) {

            }
        }

        // then
        verify(moreThan(0), getRequestedFor(urlPathMatching("/movies/(.*)")));
        verify(lessThan(n), getRequestedFor(urlPathMatching("/movies/(.*)")));
    }
}
