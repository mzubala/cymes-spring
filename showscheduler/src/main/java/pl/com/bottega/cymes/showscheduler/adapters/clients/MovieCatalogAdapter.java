package pl.com.bottega.cymes.showscheduler.adapters.clients;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.ports.MovieCatalog;

@Component
@RequiredArgsConstructor
public class MovieCatalogAdapter implements MovieCatalog {

    private final WebClient moviesClient;
    private final ReactiveCircuitBreakerFactory cbFactory;

    @Override
    public Movie get(Long movieId) {
        return moviesClient
                .get()
                .uri("/movies/{movieId}", movieId)
                .retrieve().bodyToMono(MovieResponse.class)
                .transform(it -> cbFactory.create("movies-circuit-breaker").run(it))
                .map(MovieResponse::toDomain)
                .block();
    }
}

@Data
class MovieResponse {
    private Long id;
    private Integer durationMinutes;

    Movie toDomain() {
        return new Movie(id, durationMinutes);
    }
}
