package pl.com.bottega.cymes.cinemas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElementKind;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.resources.request.SuspendRequest;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;
import pl.com.bottega.cymes.cinemas.services.dto.RowElementDto;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@IntegrationTest
public class CinemaHallResourceTest {

    @Autowired
    private PublishedEventsAssertions publishedEventsAssertions;

    @Autowired
    private CinemasClient cinemasClient;

    @Test
    public void publishesEventWhenCinemaHallIsSuspended() {
        // given
        BasicCinemaInfoDto cinema = givenCinema();
        BasicCinemaHallInfoDto cinemaHall = givenCinemaHall(cinema);

        // when
        var suspendRequest = new SuspendRequest();
        suspendRequest.setFrom(Instant.now().plus(1, DAYS));
        suspendRequest.setUntil(Instant.now().plus(2, DAYS));
        var suspendResponse = cinemasClient.suspendCinemaHall(cinemaHall.getId(), suspendRequest);

        // then
        suspendResponse.expectStatus().isOk();
        publishedEventsAssertions.awaitEventPublished(new CinemaHallSuspendedEvent(cinemaHall.getId(), suspendRequest.getFrom(), suspendRequest.getUntil()));
    }

    private BasicCinemaInfoDto givenCinema() {
        var createReq = new CreateCinemaRequest();
        createReq.setName("Arkadia");
        createReq.setCity("Warszawa");
        cinemasClient.createCinema(createReq);
        return cinemasClient.getCinemas().expectBodyList(BasicCinemaInfoDto.class).returnResult().getResponseBody().get(0);
    }

    private BasicCinemaHallInfoDto givenCinemaHall(BasicCinemaInfoDto cinema) {
        var createHallReq = new CreateCinemaHallRequest();
        createHallReq.setCinemaId(cinema.getId());
        createHallReq.setName("H1");
        var row = new RowDto();
        row.setNumber(1);
        var el = new RowElementDto();
        el.setNumber(1);
        el.setIndex(0);
        el.setKind(RowElementKind.SEAT);
        row.setElements(List.of(el));
        createHallReq.setLayout(List.of(row));
        cinemasClient.createCinemaHall(createHallReq).expectStatus().isOk();
        return cinemasClient.getCinema(cinema.getId()).expectBody(DetailedCinemaInfoDto.class).returnResult().getResponseBody().getHalls().get(0);
    }
}
