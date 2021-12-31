package pl.com.bottega.cymes.cinemas.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaHallSuspendedEvent {
    Long cinemaHallId;
    Instant from;
    Instant until;
}
