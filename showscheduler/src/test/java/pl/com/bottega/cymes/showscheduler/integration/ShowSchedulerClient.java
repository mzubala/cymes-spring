package pl.com.bottega.cymes.showscheduler.integration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;
import pl.com.bottega.cymes.showscheduler.adapters.rest.ShowSchedulerResource.ScheduleShowRequest;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class ShowSchedulerClient {

    private final WebTestClient webClient;
    private final ApplicationContext applicationContext;

    ResponseSpec scheduleShow(ScheduleShowRequest request) {
        return webClient.post()
                .uri(withHostAndPort().andThen(builder -> builder.path("/shows").build()))
                .bodyValue(request)
                .exchange();
    }

    private Function<UriBuilder, UriBuilder> withHostAndPort() {
        return (uriBuilder) -> uriBuilder
                .host("localhost")
                .port(applicationContext.getEnvironment().getProperty("local.server.port"));
    }

}
