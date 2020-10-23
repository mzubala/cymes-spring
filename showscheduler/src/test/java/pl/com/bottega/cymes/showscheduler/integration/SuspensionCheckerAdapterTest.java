package pl.com.bottega.cymes.showscheduler.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.SuspensionCheckerAdapter;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import javax.inject.Inject;
import javax.ws.rs.ServerErrorException;
import java.util.Date;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(Arquillian.class)
public class SuspensionCheckerAdapterTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(options().port(8888).httpsPort(8889));

    @Inject
    private SuspensionCheckerAdapter suspensionCheckerAdapter;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Test
    public void returnsFalseWhenNeitherCinemaNotCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaSuspensionCheckReturns(show, false);
        cinemaHallSuspensionCheckReturns(show, false);

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
        cinemaSuspensionCheckReturns(show, randBool);
        cinemaHallSuspensionCheckReturns(show, !randBool);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void returnsTrueWhenBothCinemaAndCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaSuspensionCheckReturns(show, true);
        cinemaHallSuspensionCheckReturns(show, true);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void throwsExceptionWhenOneOfTheDependenciesFails() {
        // given
        var show = new ShowExample().toShow();
        cinemaSuspensionCheckReturnsError(show);
        cinemaHallSuspensionCheckReturns(show, true);

        // then
        assertThatThrownBy(() -> suspensionCheckerAdapter.anySuspensionsAtTimeOf(show)).hasCauseInstanceOf(ServerErrorException.class);
    }

    private void cinemaHallSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/halls/", show.getCinemaHallId());
    }

    private void cinemaSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/cinemas/", show.getCinemaId());
    }

    private void cinemaHallSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError("/halls/", show.getCinemaHallId());
    }

    private void cinemaSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError("/cinemas/", show.getCinemaId());
    }

    private void stubSuspensionCheck(Show show, boolean value, String url, Long id) {
        wireMock.stubFor(get(urlPathEqualTo(url + id + "/suspensions"))
            .withQueryParam("from", new EqualToPattern(Date.from(show.getStart()).toString()))
            .withQueryParam("until", new EqualToPattern(Date.from(show.getEnd()).toString()))
            .willReturn(aResponse()
                .withBody(suspendedBody(value))
                .withHeader("Content-type", "application/json")
            ));
    }

    private void stubSuspensionCheckError(String url, Long cinemaHallId) {
        wireMock.stubFor(get(urlPathEqualTo(url + cinemaHallId + "/suspensions"))
            .willReturn(aResponse()
                .withStatus(500)
            ));
    }

    private String suspendedBody(boolean value) {
        return "{\"suspended\": " + value + "}";
    }
}
