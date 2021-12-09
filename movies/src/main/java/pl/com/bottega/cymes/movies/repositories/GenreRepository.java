package pl.com.bottega.cymes.movies.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.bottega.cymes.movies.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
