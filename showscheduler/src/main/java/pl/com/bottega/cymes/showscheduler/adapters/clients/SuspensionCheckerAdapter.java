package pl.com.bottega.cymes.showscheduler.adapters.clients;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ports.SuspensionChecker;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class SuspensionCheckerAdapter implements SuspensionChecker {

    private final WebClient cinemasClient;
    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    @Override
    public boolean anySuspensionsAtTimeOf(Show show) {
        return getSuspension(show, "cinemas", show.getCinemaId())
                .zipWith(getSuspension(show, "halls", show.getCinemaHallId()))
                .transform(it -> circuitBreakerFactory.create("cinemas-circuit-breaker").run(it))
                .map(responses -> responses.getT1().isSuspended() || responses.getT2().isSuspended())
                .onErrorResume(ex -> Mono.error(new ServerErrorException("Failed to get suspension", ex)))
                .block();
    }

    private Mono<SuspensionResponse> getSuspension(Show show, String object, Long id) {
        return cinemasClient
                .get()
                .uri(builder ->
                        builder
                                .path("/" + object + "/{id}/suspensions")
                                .queryParam("from", show.getStart())
                                .queryParam("until", show.getEnd())
                                .build(id)

                )
                .retrieve().bodyToMono(SuspensionResponse.class);
    }
}

@Data
class SuspensionResponse {
    private boolean suspended;
}
