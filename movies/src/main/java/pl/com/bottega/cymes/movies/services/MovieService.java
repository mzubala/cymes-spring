package pl.com.bottega.cymes.movies.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.movies.model.Movie;
import pl.com.bottega.cymes.movies.repositories.MovieRepository;

@Component
@Log
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public void create(CreateMovieCommand command) {
        var movie = new Movie();
        movie.setTitle(command.getTitle());
        movie.setDurationMinutes(command.getDurationMinutes());
        movieRepository.save(movie);
    }

    public void update(UpdateMovieCommand command) {

    }

    public void publish(PublishMovieCommand command) {

    }

    public void archive(ArchiveMovieCommand command) {

    }
}
