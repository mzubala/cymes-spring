package pl.com.bottega.cymes.movies.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Component
public class MovieFinder {

    public MovieDetails findById(Long id, Set<MovieAttribute> movieAttributes) {
        return null;
    }

    public List<MovieDetails> search(SearchParams params) {
        return null;
    }

    public static enum MovieAttribute {
        TITLE, DIRECTOR, ACTORS, GENRES, DURATION
    }

    @Data
    public
    class SearchParams {
        private String phrase;
        private Long genreId;
        private List<MovieAttribute> attributes;
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class MovieDetails {
        private Long id;
        private String title;
        private DirectorDetails directorId;
        private Set<ActorDetails> actorIds;
        private Set<GenreDetails> genreIds;
        private Integer durationMinutes;
    }

    @Data
    public static class DirectorDetails {
        private Long id;
        private String name;
    }

    @Data
    public static class ActorDetails {
        private Long id;
        private String name;
    }

    @Data
    public static class GenreDetails {
        private Long id;
        private String name;
    }
}
