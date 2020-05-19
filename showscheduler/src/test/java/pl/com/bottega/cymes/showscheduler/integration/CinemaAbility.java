package pl.com.bottega.cymes.showscheduler.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import pl.com.bottega.cymes.showscheduler.domain.Show;

import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

class CinemaAbility {
    private final WireMockRule wireMock;

    CinemaAbility(WireMockRule wireMock) {
        this.wireMock = wireMock;
    }

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

    void cinemaSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError("/cinemas/", show.getCinemaId());
    }

    void cinemaHallSuspensionCheckReturnsError(Show show) {
        stubSuspensionCheckError("/halls/", show.getCinemaHallId());
    }

    private String suspendedBody(boolean value) {
        return "{\"suspended\": " + value + "}";
    }

    private void stubSuspensionCheck(Long id, boolean value, String url) {
        wireMock.stubFor(get(urlPathEqualTo(url + id + "/suspensions"))
            .willReturn(aResponse()
                .withBody(suspendedBody(value))
                .withHeader("Content-type", "application/json")
            ));
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
}
