package pl.com.bottega.cymes.movies.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.bottega.cymes.movies.model.Star;

public interface StarRepository extends JpaRepository<Star, Long> {
}
