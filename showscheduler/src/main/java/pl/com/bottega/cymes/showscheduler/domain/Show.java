package pl.com.bottega.cymes.showscheduler.domain;

import pl.com.bottega.cymes.showscheduler.domain.ports.ShowSchedulerConfiguration;

import java.time.Instant;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Show {
    private final UUID id;
    private final Long movieId;
    private final Long cinemaId;
    private Long cinemaHallId;
    private Instant start;
    private Instant end;

    Show(ScheduleShowCommand cmd, Movie movie, ShowSchedulerConfiguration configuration) {
        this.id = cmd.getShowId();
        this.movieId = movie.getId();
        this.cinemaId = cmd.getCinemaId();
        this.cinemaHallId = cmd.getCinemaHallId();
        this.start = cmd.getWhen();
        this.end = start.plus(movie.getDurationMinutes(), MINUTES).plus(configuration.showReservationBuffer());
    }

    public Show(UUID id, Long movieId, Long cinemaId, Long cinemaHallId, Instant start, Instant end) {
        this.id = id;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.cinemaHallId = cinemaHallId;
        this.start = start;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Long getCinemaHallId() {
        return cinemaHallId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getCinemaId() {
        return cinemaId;
    }
}
