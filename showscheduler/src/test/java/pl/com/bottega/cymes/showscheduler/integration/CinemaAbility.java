package pl.com.bottega.cymes.showscheduler.integration;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.domain.Show;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@Component
class CinemaAbility {

    void cinemaHallSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/halls/", show.getCinemaHallId());
    }

    void cinemaSuspensionCheckReturns(Show show, boolean value) {
        stubSuspensionCheck(show, value, "/cinemas/", show.getCinemaId());
    }

    void cinemaHallSuspensionCheckReturns(Long cinemaHallId, boolean value) {
        stubSuspensionCheck(cinemaHallId, value, "/halls/");
    }

    void cinemaSuspensionCheckReturns(Long cinemaId, boolean value) {
        stubSuspensionCheck(cinemaId, value, "/cinemas/");
    }

    void cinemaHallSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError(show, "/halls/", show.getCinemaHallId());
    }

    void cinemaSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError(show, "/cinemas/", show.getCinemaId());
    }

    private void stubSuspensionCheck(Long id, boolean value, String url) {
        stubFor(get(urlPathEqualTo(url + id + "/suspensions"))
                .willReturn(aResponse()
                        .withBody(suspendedBody(value))
                        .withHeader("Content-type", "application/json")
                ));
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
