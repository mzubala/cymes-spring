package pl.com.bottega.cymes.cinemas.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaSuspendedEvent {
    Long cinemaId;
    Instant from;
    Instant until;
}
