package pl.com.bottega.cymes.showscheduler.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Movie {

    @NonNull
    private final Long id;

    @NonNull
    private final Integer durationMinutes;

}
