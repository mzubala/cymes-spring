package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor
public class ShowExample {
    private UUID id = UUID.randomUUID();
    private Long movieId = Math.abs(new Random().nextLong());
    private Long cinemaId = Math.abs(new Random().nextLong());
    private Long cinemaHallId = Math.abs(new Random().nextLong());
    private Instant start = Instant.now().plusMillis(Math.abs(new Random().nextInt(200000)));
    private Instant end = start.plusMillis(Math.abs(new Random().nextInt(200000)));
    private boolean canceled;

    public Show toShow() {
        return new Show(id, movieId, cinemaId, cinemaHallId, start, end, canceled, null);
    }
}
