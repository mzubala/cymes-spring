package pl.com.bottega.cymes.cinemas;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.resources.request.SuspendRequest;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CinemasClient {
    private final WebTestClient webTestClient;
    private final ApplicationContext applicationContext;

    public ResponseSpec createCinema(CreateCinemaRequest request) {
        return webTestClient
                .post()
                .uri(withHostAndPort().andThen((uriBuilder) -> uriBuilder.path("/cinemas").build()))
                .bodyValue(request)
                .exchange();
    }

    public ResponseSpec getCinemas() {
        return webTestClient
                .get()
                .uri(withHostAndPort().andThen((uriBuilder) -> uriBuilder.path("/cinemas").build()))
                .exchange();
    }

    public ResponseSpec getCinema(Long id) {
        return webTestClient
            .get()
            .uri(withHostAndPort().andThen((uriBuilder) -> uriBuilder.path("/cinemas/{id}").build(id)))
            .exchange();
    }

    public ResponseSpec suspendCinema(Long cinemaId, SuspendRequest request) {
        return webTestClient.post()
            .uri(withHostAndPort().andThen(uriBuilder -> uriBuilder.path("/cinemas/{id}/suspensions").build(cinemaId)))
            .bodyValue(request)
            .exchange();
    }

    public ResponseSpec createCinemaHall(CreateCinemaHallRequest request) {
        return webTestClient.post()
            .uri(withHostAndPort().andThen(uriBuilder -> uriBuilder.path("/halls").build()))
            .bodyValue(request)
            .exchange();
    }

    public ResponseSpec suspendCinemaHall(Long cinemaHallId, SuspendRequest request) {
        return webTestClient.post()
            .uri(withHostAndPort().andThen(uriBuilder -> uriBuilder.path("/halls/{id}/suspensions").build(cinemaHallId)))
            .bodyValue(request)
            .exchange();
    }

    private Function<UriBuilder, UriBuilder> withHostAndPort() {
        return (uriBuilder) -> uriBuilder
                .host("localhost")
                .port(applicationContext.getEnvironment().getProperty("local.server.port"));
    }
}
