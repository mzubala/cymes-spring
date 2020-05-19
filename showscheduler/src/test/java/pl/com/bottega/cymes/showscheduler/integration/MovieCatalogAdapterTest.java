package pl.com.bottega.cymes.showscheduler.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.hystrix.Hystrix;
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

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static pl.com.bottega.cymes.showscheduler.integration.DeploymentFactory.wireMockRule;

@RunWith(Arquillian.class)
public class MovieCatalogAdapterTest {

    @Rule
    public WireMockRule wireMock = wireMockRule();

    @Inject
    private MovieCatalogAdapter movieCatalogAdapter;

    private MovieAbility movieAbility;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Before
    public void setup() {
        movieAbility = new MovieAbility(wireMock);
        Hystrix.reset();
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
    public void usesCircuitBreaker() {
        // given
        var movie = new Movie(1L, 120);
        movieAbility.stubMovieError(movie);

        // when
        IntStream.range(1, 100).forEach((i) -> {
            assertThatThrownBy(() -> movieCatalogAdapter.get(movie.getId()));
        });

        // then
        assertThat(wireMock.findAll(getRequestedFor(urlPathEqualTo("/movies/" + movie.getId()))).size()).isLessThan(99);
    }
}
