package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.lessThan;
import static com.github.tomakehurst.wiremock.client.WireMock.moreThan;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class MovieCatalogAdapterTest {

    @Autowired
    private MovieCatalog movieCatalogAdapter;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void resetCircuitBreaker() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> cb.reset());
    }

    @Test
    public void returnsMovie() {
        // given
        var movie = new Movie(1L, 120);
        stubMovie(movie);

        // when
        var fetchedMovie = movieCatalogAdapter.get(movie.getId());

        // then
        assertThat(fetchedMovie).isEqualTo(movie);
    }

    @Test
    public void usesACircuitBreaker() {
        // given
        stubMoviesServiceFailure();
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

    @SneakyThrows
    private void stubMovie(Movie movie) {
        stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withBody(objectMapper.writeValueAsString(movie))
                .withHeader("Content-type", "application/json")
            ));
    }

    private void stubMoviesServiceFailure() {
        stubFor(get(urlPathMatching("/movies/(.*)"))
                .willReturn(aResponse()
                        .withStatus(500)
                ));
    }
}
