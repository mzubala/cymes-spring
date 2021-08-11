package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;
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
                .exchangeToMono(this::toSuspensionResponse);
    }

    private Mono<SuspensionResponse> toSuspensionResponse(ClientResponse response) {
        if(response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(SuspensionResponse.class);
        } else {
            return Mono.error(new ServerErrorException("Failed to get suspension", new IllegalStateException()));
        }
    }
}

@Data
class SuspensionResponse {
    private boolean suspended;
}
