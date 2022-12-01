package pl.com.bottega.cymes.movies.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@RestController
@RequestMapping("/movies")
public class MoviesResource {

    // 1. Create movie
    // 2. Update movie
    // 3. Publish movie
    // 4. Archive movie
    // 5. Get by id
    // 6. Search movies

    @PostMapping
    public void createMovie(@Valid @RequestBody CreateMovieRequest request) {

    }

    @PutMapping(path = "/{id}")
    public void updateMovie(@PathVariable Long id, @RequestBody UpdateMovieRequest request) {

    }

    @PutMapping(path = "/{id}/publish")
    public void publishMovie(@PathVariable Long id) {

    }

    @PutMapping(path = "/{id}/archive")
    public void archiveMovie(@PathVariable Long id) {

    }

    @GetMapping(path = "/{id}")
    public MovieDetails getMovie(@PathVariable Long id, @RequestParam List<MovieAttribute> attributes) {
        return null;
    }

    @GetMapping
    public List<MovieDetails> search(SearchParams params) {
        return null;
    }
}

@Data
class SearchParams {
    private String phrase;
    private Long genreId;
    private List<MovieAttribute> attributes;
}

@Data
class CreateMovieRequest {
    @NotBlank
    private String title;

    @NotNull
    private Long directorId;

    @NotEmpty
    private Set<Long> actorIds;

    @NotEmpty
    private Set<Long> genreIds;

    @NotNull
    @Min(30)
    @Max(24*60)
    private Integer durationMinutes;
}

@Data
class UpdateMovieRequest {
    private String title;
    private Long directorId;
    private Set<Long> actorIds;
    private Set<Long> genreIds;
    private Integer durationMinutes;
}

@Data
@JsonInclude(NON_NULL)
class MovieDetails {
    private Long id;
    private String title;
    private DirectorDetails directorId;
    private Set<ActorDetails> actorIds;
    private Set<GenreDetails> genreIds;
    private Integer durationMinutes;
}

@Data
class DirectorDetails {
    private Long id;
    private String name;
}

@Data
class ActorDetails {
    private Long id;
    private String name;
}

@Data
class GenreDetails {
    private Long id;
    private String name;
}

enum MovieAttribute {
    TITLE, DIRECTOR, ACTORS, GENRES, DURATION
}