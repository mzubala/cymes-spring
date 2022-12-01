package pl.com.bottega.cymes.movies.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.movies.services.ArchiveMovieCommand;
import pl.com.bottega.cymes.movies.services.CreateMovieCommand;
import pl.com.bottega.cymes.movies.services.MovieFinder;
import pl.com.bottega.cymes.movies.services.MovieFinder.MovieAttribute;
import pl.com.bottega.cymes.movies.services.MovieFinder.MovieDetails;
import pl.com.bottega.cymes.movies.services.MovieFinder.SearchParams;
import pl.com.bottega.cymes.movies.services.MovieService;
import pl.com.bottega.cymes.movies.services.PublishMovieCommand;
import pl.com.bottega.cymes.movies.services.UpdateMovieCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/movies")
@Log
public class MoviesResource {

    private final MovieService movieService;
    private final MovieFinder movieFinder;

    public MoviesResource(MovieService movieService, MovieFinder movieFinder) {
        this.movieService = movieService;
        this.movieFinder = movieFinder;
        log.info("Movie service class = " + movieService.getClass().toString());
    }

    @PostMapping
    public void createMovie(@Valid @RequestBody CreateMovieRequest request) {
        movieService.create(request.toCommand());
    }

    @PostMapping("/sample")
    public void createSampleMovie() {
        movieService.createSampleMovie();
    }

    @PutMapping(path = "/{id}")
    public void updateMovie(@PathVariable Long id, @RequestBody UpdateMovieRequest request) {
        movieService.update(request.toCommand(id));
    }

    @PutMapping(path = "/{id}/publish")
    public void publishMovie(@PathVariable Long id) {
        movieService.publish(new PublishMovieCommand(id));
    }

    @PutMapping(path = "/{id}/archive")
    public void archiveMovie(@PathVariable Long id) {
        movieService.archive(new ArchiveMovieCommand(id));
    }

    @GetMapping(path = "/{id}")
    public MovieDetails getMovie(@PathVariable Long id, @RequestParam List<MovieAttribute> attributes) {
        return movieFinder.findById(id, new HashSet<>(attributes));
    }

    @GetMapping
    public List<MovieDetails> search(SearchParams params) {
        return movieFinder.search(params);
    }
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

    public CreateMovieCommand toCommand() {
        return new CreateMovieCommand(title, directorId, actorIds, genreIds, durationMinutes);
    }
}

@Data
class UpdateMovieRequest {
    private String title;
    private Long directorId;
    private Set<Long> actorIds;
    private Set<Long> genreIds;
    private Integer durationMinutes;

    public UpdateMovieCommand toCommand(Long id) {
        return new UpdateMovieCommand(id, title, directorId, actorIds, genreIds, durationMinutes);
    }
}