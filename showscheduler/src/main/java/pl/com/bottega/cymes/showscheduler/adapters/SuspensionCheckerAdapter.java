package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;

@RequiredArgsConstructor
@Component
public class SuspensionCheckerAdapter implements SuspensionChecker {

    private final RestTemplate cinemasRestTemplate;

    @Override
    public boolean anySuspensionsAtTimeOf(Show show) {
        return false;
    }
}
