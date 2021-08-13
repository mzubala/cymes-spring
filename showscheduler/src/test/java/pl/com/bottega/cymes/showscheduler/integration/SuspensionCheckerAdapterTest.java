package pl.com.bottega.cymes.showscheduler.integration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.adapters.clients.SuspensionCheckerAdapter;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.lessThan;
import static com.github.tomakehurst.wiremock.client.WireMock.moreThan;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@IntegrationTest
public class SuspensionCheckerAdapterTest {

    @Autowired
    private SuspensionCheckerAdapter suspensionCheckerAdapter;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CinemaAbility cinemaAbility;

    @BeforeEach
    public void resetCircuitBreaker() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> cb.reset());
    }

    @Test
    public void returnsFalseWhenNeitherCinemaNotCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturns(show, false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, false);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void returnsTrueWhenEitherCinemaOrCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        var randBool = new Random().nextDouble() > 0.5;
        cinemaAbility.cinemaSuspensionCheckReturns(show, randBool);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, !randBool);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void returnsTrueWhenBothCinemaAndCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturns(show, true);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, true);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void throwsExceptionWhenOneOfTheDependenciesFails() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturnsError(show);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, true);

        // then
        assertThatThrownBy(() -> suspensionCheckerAdapter.anySuspensionsAtTimeOf(show)).isInstanceOf(ServerErrorException.class);
    }

    @Test
    public void usesACircuitBreaker() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturnsError(show);
        cinemaAbility.cinemaHallSuspensionCheckReturnsError(show);
        int n = 200;

        // when
        for(int i = 0; i<n; i++) {
            try {
                suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);
            } catch (Exception ex) {

            }
        }

        // then
        verify(moreThan(0), getRequestedFor(urlPathMatching("/cinemas/(.*)/suspensions")));
        verify(lessThan(2*n), getRequestedFor(urlPathMatching("/cinemas/(.*)/suspensions")));
        verify(moreThan(0), getRequestedFor(urlPathMatching("/halls/(.*)/suspensions")));
        verify(lessThan(2*n), getRequestedFor(urlPathMatching("/halls/(.*)/suspensions")));
    }
}
