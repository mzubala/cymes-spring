package pl.com.bottega.cymes.movies.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.movies.model.Genre;
import pl.com.bottega.cymes.movies.model.Movie;
import pl.com.bottega.cymes.movies.model.Star;
import pl.com.bottega.cymes.movies.repositories.GenreRepository;
import pl.com.bottega.cymes.movies.repositories.MovieRepository;
import pl.com.bottega.cymes.movies.repositories.StarRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final StarRepository starRepository;
    private final GenreRepository genreRepository;

    public void create(CreateMovieCommand command) {
        var movie = new Movie();
        movie.setTitle(command.getTitle());
        movie.setDurationMinutes(command.getDurationMinutes());
        movie.setActors(fetchActorReferences(command));
        movie.setDirector(starRepository.getById(command.getDirectorId()));
        movie.setGeneres(fetchGenresReferences(command));
        movieRepository.save(movie);
    }

    @Transactional
    public void createSampleMovie() {
        var movie = new Movie();
        movie.setTitle("Pulp Fiction");
        movie.setDurationMinutes(120);
        var stars = List.of(
            new Star(null, "John", null, "Trevolta"),
            new Star(null, "Samuel", "L", "Jackson")
        );
        movie.setActors(stars);
        starRepository.saveAll(stars);
        var director = new Star(null, "Quentin", null, "Tarantino");
        movie.setDirector(director);
        starRepository.save(director);
        var genres = Set.of(new Genre(null, "Drama"));
        genreRepository.saveAll(genres);
        movie.setGeneres(genres);
        movieRepository.save(movie);
    }

    public void update(UpdateMovieCommand command) {

    }

    public void publish(PublishMovieCommand command) {

    }

    public void archive(ArchiveMovieCommand command) {

    }

    private Set<Genre> fetchGenresReferences(CreateMovieCommand command) {
        return command.getGenreIds().stream().map(genreRepository::getById).collect(Collectors.toSet());
    }

    private List<Star> fetchActorReferences(CreateMovieCommand command) {
        return command.getActorIds().stream().map(starRepository::getById).collect(Collectors.toList());
    }
}
