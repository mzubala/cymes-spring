package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

@With
@AllArgsConstructor
@NoArgsConstructor
class ScheduleShowCommandExample {
    private UUID showId = UUID.randomUUID();
    private Long userId = new Random().nextLong();
    private Long movieId = new Random().nextLong();
    private Long cinemaId = new Random().nextLong();
    private Long cinemaHallId = new Random().nextLong();
    private Instant when = Instant.now().plus(new Random().nextInt(200), DAYS);

    ScheduleShowCommand toCommand() {
        return ScheduleShowCommand.builder()
            .showId(showId)
            .cinemaHallId(cinemaHallId)
            .cinemaId(cinemaId)
            .movieId(movieId)
            .userId(userId)
            .when(when)
            .build();
    }
}
