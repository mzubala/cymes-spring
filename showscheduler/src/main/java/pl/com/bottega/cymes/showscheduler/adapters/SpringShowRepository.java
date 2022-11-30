package pl.com.bottega.cymes.showscheduler.adapters;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowRepository;

import java.time.Instant;
import java.util.UUID;

import static pl.com.bottega.cymes.showscheduler.adapters.ShowEntity.COLLIDING_SHOWS_PRESENT;

@Component
@RequiredArgsConstructor
public class SpringShowRepository implements ShowRepository {

    private final SpringDataShowRepository springDataShowRepository;

    @Override
    @Transactional
    public void save(Show show) {
        springDataShowRepository.save(new ShowEntity(show));
    }

    @Override
    public boolean anyShowsCollidingWith(Show show) {
        return springDataShowRepository.countCollidingShows(
                show.getStart(),
                show.getEnd(),
                show.getCinemaId(),
                show.getCinemaHallId()
        );
    }

    @Override
    public Show get(UUID showId) {
        return springDataShowRepository.findById(showId).map(ShowEntity::toShow)
                .orElseThrow(ShowNotFoundException::new);
    }
}

interface SpringDataShowRepository extends JpaRepository<ShowEntity, UUID> {
        boolean countCollidingShows(Instant ns, Instant ne, Long cinemaId, Long cinemaHallId);
}

@Entity(name = "Show")
@Table(name = "shows")
@NoArgsConstructor
@NamedQuery(
        name = COLLIDING_SHOWS_PRESENT,
        query = "SELECT (count(s) > 0) FROM Show s WHERE " +
                "((:ns <= s.start AND :ne >= s.start AND :ne <= s.end) OR " +
                "(:ns <= s.start AND :ne >= s.end)  OR" +
                "(:ns >= s.start AND :ne <= s.end) OR " +
                "(:ns >= s.start AND :ns <= s.end AND :ne >= s.end)) AND " +
                "s.cinemaId = :cinemaId AND " +
                "s.cinemaHallId = :cinemaHallId"
)
class ShowEntity {

    static final String COLLIDING_SHOWS_PRESENT = "ShowEntity.countCollidingShows";

    @Id
    private UUID id;
    private Long movieId;
    private Long cinemaId;
    private Long cinemaHallId;
    private Instant start;
    private Instant end;

    ShowEntity(Show show) {
        this.id = show.getId();
        this.movieId = show.getMovieId();
        this.cinemaId = show.getCinemaId();
        this.cinemaHallId = show.getCinemaHallId();
        this.start = show.getStart();
        this.end = show.getEnd();
    }

    Show toShow() {
        return new Show(id, movieId, cinemaId, cinemaHallId, start, end);
    }
}
