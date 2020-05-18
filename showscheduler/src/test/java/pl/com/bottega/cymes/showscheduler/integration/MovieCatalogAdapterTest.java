package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.MovieCatalogAdapter;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import javax.inject.Inject;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @SneakyThrows
    private void stubMovie(Movie movie) {
        wireMock.stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withBody(objectMapper.writeValueAsString(movie))
                .withHeader("Content-type", "application/json")
            ));
    }
}
