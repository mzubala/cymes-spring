package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@Component
class MovieAbility {

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    void stubMovie(Movie movie) {
        stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
                .willReturn(aResponse()
                        .withBody(objectMapper.writeValueAsString(movie))
                        .withHeader("Content-type", "application/json")
                ));
    }

    void stubMovieError(Movie movie) {
        stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
                .willReturn(aResponse()
                        .withStatus(500)
                ));
    }

    void stubMoviesServiceFailure() {
        stubFor(get(urlPathMatching("/movies/(.*)"))
                .willReturn(aResponse()
                        .withStatus(500)
                ));
    }
}
