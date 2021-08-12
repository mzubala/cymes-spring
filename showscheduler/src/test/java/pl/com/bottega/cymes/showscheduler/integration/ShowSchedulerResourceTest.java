package pl.com.bottega.cymes.showscheduler.integration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import pl.com.bottega.cymes.showscheduler.adapters.ShowSchedulerResource.ScheduleShowRequest;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import java.time.Instant;
import java.util.UUID;


@IntegrationTest
public class ShowSchedulerResourceTest {

    @Autowired
    private MovieAbility movieAbility;

    @Autowired
    private CinemaAbility cinemaAbility;

    @Test
    public void schedulesShow() {
        // given
        var scheduleShowRequest = new ScheduleShowRequest(UUID.randomUUID().toString(), 1L, 2L, 3L, Instant.now());
        cinemaAbility.cinemaSuspensionCheckReturns(scheduleShowRequest.getCinemaId(), false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(scheduleShowRequest.getCinemaHallId(), false);
        movieAbility.stubMovie(new Movie(scheduleShowRequest.getMovieId(), 120));

        // when
        var response = scheduleShow(scheduleShowRequest);

        response.expectStatus().isOk();
    }

    @SneakyThrows
    private ResponseSpec scheduleShow(ScheduleShowRequest request) {
        return null;
    }
}
