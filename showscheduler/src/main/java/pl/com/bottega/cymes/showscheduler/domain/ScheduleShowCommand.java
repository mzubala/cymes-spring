package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class ScheduleShowCommand {
    @NonNull
    private final UUID showId;
    @NonNull
    private final Long userId;
    @NonNull
    private final Long movieId;
    @NonNull
    private final Long cinemaId;
    @NonNull
    private final Long cinemaHallId;
    @NonNull
    private final Instant when;
}
