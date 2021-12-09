package pl.com.bottega.cymes.movies.services;

import lombok.Value;

import java.util.Set;

@Value
public class UpdateMovieCommand {
    Long id;
    String title;
    Long directorId;
    Set<Long> actorIds;
    Set<Long> genreIds;
    Integer durationMinutes;
}
