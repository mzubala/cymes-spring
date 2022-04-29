package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class MovieCatalogAdapterTest {

    @Autowired
    @Qualifier("movieCatalogWebClientAdapter")
    private MovieCatalog movieCatalog;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void returnsMovie() {
        // given
        var movie = new Movie(1L, 120);
        stubMovie(movie);

        // when
        var fetchedMovie = movieCatalog.get(movie.getId());

        // then
        assertThat(fetchedMovie).isEqualTo(movie);
    }

    @SneakyThrows
    private void stubMovie(Movie movie) {
        stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withBody(objectMapper.writeValueAsString(movie))
                .withHeader("Content-type", "application/json")
            ));
    }
}
