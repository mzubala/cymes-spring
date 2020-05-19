package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

class MovieAbility {
    private final WireMockRule wireMock;

    private ObjectMapper objectMapper = new ObjectMapper();

    MovieAbility(WireMockRule wireMock) {
        this.wireMock = wireMock;
    }

    @SneakyThrows
    void stubMovie(Movie movie) {
        wireMock.stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withBody(objectMapper.writeValueAsString(movie))
                .withHeader("Content-type", "application/json")
            ));
    }

    void stubMovieError(Movie movie) {
        wireMock.stubFor(get(urlPathEqualTo("/movies/" + movie.getId()))
            .willReturn(aResponse()
                .withStatus(500)
            ));
    }
}
