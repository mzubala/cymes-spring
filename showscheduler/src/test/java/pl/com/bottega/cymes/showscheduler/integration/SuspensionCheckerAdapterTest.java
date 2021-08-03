package pl.com.bottega.cymes.showscheduler.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.adapters.SuspensionCheckerAdapter;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@IntegrationTest
public class SuspensionCheckerAdapterTest {

    @Autowired
    private SuspensionCheckerAdapter suspensionCheckerAdapter;

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
        assertThatThrownBy(() -> suspensionCheckerAdapter.anySuspensionsAtTimeOf(show)).isInstanceOf(ServerErrorException.class);
    }

    private void cinemaHallSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/halls/", show.getCinemaHallId());
    }

    private void cinemaSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/cinemas/", show.getCinemaId());
    }

    private void cinemaHallSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError(show, "/halls/", show.getCinemaHallId());
    }

    private void cinemaSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError(show, "/cinemas/", show.getCinemaId());
    }

    private void stubSuspensionCheck(Show show, boolean value, String url, Long id) {
        stubFor(get(urlPathEqualTo(url + id + "/suspensions"))
            .withQueryParam("from", equalTo(show.getStart().toString()))
            .withQueryParam("until", equalTo(show.getEnd().toString()))
            .willReturn(aResponse()
                .withBody(suspendedBody(value))
                .withHeader("Content-type", "application/json")
            ));
    }

    private void stubSuspensionCheckError(Show show, String url, Long cinemaHallId) {
        stubFor(get(urlPathMatching(url + cinemaHallId + "/suspensions"))
            .willReturn(aResponse()
                .withStatus(500)
            ));
    }

    private String suspendedBody(boolean value) {
        return "{\"suspended\": " + value + "}";
    }
}
