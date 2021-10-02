package pl.com.bottega.cymes.showscheduler.adapters.db;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.Instant;
import java.util.UUID;

import static pl.com.bottega.cymes.showscheduler.adapters.db.ShowEntity.COLLIDING_SHOWS_PRESENT;

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
        return springDataShowRepository.anyShowsCollidingWith(
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

    public long countShows() {
        return springDataShowRepository.count();
    }
}

interface SpringDataShowRepository extends JpaRepository<ShowEntity, UUID> {
        @Query(name = COLLIDING_SHOWS_PRESENT)
        boolean anyShowsCollidingWith(Instant ns, Instant ne, Long cinemaId, Long cinemaHallId);
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
    @Version
    private Long version;

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
