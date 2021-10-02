package pl.com.bottega.cymes.showscheduler.integration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import pl.com.bottega.cymes.showscheduler.adapters.rest.ShowSchedulerResource.ScheduleShowRequest;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@IntegrationTest
public class ShowSchedulerResourceTest {

    @Autowired
    private MovieAbility movieAbility;

    @Autowired
    private CinemaAbility cinemaAbility;

    @Autowired
    private ShowSchedulerClient client;

    @Test
    public void schedulesShow() {
        // given
        var scheduleShowRequest = new ScheduleShowRequest(UUID.randomUUID().toString(), 1L, 2L, 3L, Instant.now());
        cinemaAbility.cinemaSuspensionCheckReturns(scheduleShowRequest.getCinemaId(), false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(scheduleShowRequest.getCinemaHallId(), false);
        movieAbility.stubMovie(new Movie(scheduleShowRequest.getMovieId(), 120));

        // when
        var response = client.scheduleShow(scheduleShowRequest);

        response.expectStatus().isOk();
    }

    @Test
    public void respondsWithConflictWhenCinemaHallIsOccupied() {
        // given
        var firstRequest = new ScheduleShowRequest(UUID.randomUUID().toString(), 1L, 2L, 3L, Instant.now());
        var secondRequest = new ScheduleShowRequest(UUID.randomUUID().toString(), 1L, 2L, 3L, Instant.now());
        cinemaAbility.cinemaSuspensionCheckReturns(secondRequest.getCinemaId(), false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(secondRequest.getCinemaHallId(), false);
        movieAbility.stubMovie(new Movie(secondRequest.getMovieId(), 120));
        client.scheduleShow(firstRequest);

        // when
        client.scheduleShow(firstRequest);
        var response =  client.scheduleShow(secondRequest);

        response.expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void respondsWithConflictWhenTryingToScheduleShowWithExisitngId() {
        // given
        var firstRequest = new ScheduleShowRequest(UUID.randomUUID().toString(), 1L, 2L, 3L, Instant.now());
        var secondRequest = new ScheduleShowRequest(firstRequest.getShowId(), 1L, 2L, 3L, Instant.now().plus(Duration.ofHours(24)));
        cinemaAbility.cinemaSuspensionCheckReturns(firstRequest.getCinemaId(), false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(firstRequest.getCinemaHallId(), false);
        movieAbility.stubMovie(new Movie(firstRequest.getMovieId(), 120));

        // when
        var firstResponse = client.scheduleShow(firstRequest);
        var secondResponse =  client.scheduleShow(secondRequest);

        // then
        firstResponse.expectStatus().isOk();
        secondResponse.expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
}
