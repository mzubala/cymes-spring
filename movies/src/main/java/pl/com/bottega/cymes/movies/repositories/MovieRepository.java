package pl.com.bottega.cymes.movies.repositories;

import org.springframework.data.repository.Repository;
import pl.com.bottega.cymes.movies.model.Movie;

public interface MovieRepository extends Repository<Movie, Long> {
    void save(Movie movie);
    Movie findById(Long id);
    Movie findByIdAndTitle(Long id, String title);
}
