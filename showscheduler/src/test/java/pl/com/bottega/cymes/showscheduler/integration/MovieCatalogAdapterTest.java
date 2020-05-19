package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.hystrix.Hystrix;
import lombok.SneakyThrows;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.MovieCatalogAdapter;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import javax.inject.Inject;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(Arquillian.class)
public class MovieCatalogAdapterTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(options().port(8888).httpsPort(8889));

    @Inject
    private MovieCatalogAdapter movieCatalogAdapter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Before
    public void setup() {
        Hystrix.reset();
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
    public void usesCircuitBreaker() {
        // given
        var movie = new Movie(1L, 120);
        stubMovieError(movie);

        // when
        IntStream.range(1, 100).forEach((i) -> {
            assertThatThrownBy(() -> movieCatalogAdapter.get(movie.getId()));
        });

        // then
        assertThat(wireMock.findAll(getRequestedFor(urlPathEqualTo("/movies/" + movie.getId()))).size()).isLessThan(99);
    }

    @SneakyThrows
    private void stubMovie(Movie movie) {
        wireMock.stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withBody(objectMapper.writeValueAsString(movie))
                .withHeader("Content-type", "application/json")
            ));
    }

    private void stubMovieError(Movie movie) {
        wireMock.stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withStatus(500)
            ));
    }
}
