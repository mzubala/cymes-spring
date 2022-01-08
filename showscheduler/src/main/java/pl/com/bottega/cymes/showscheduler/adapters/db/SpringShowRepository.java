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
import java.util.stream.Stream;

import static pl.com.bottega.cymes.showscheduler.adapters.db.ShowEntity.COLLIDING_SHOWS_PRESENT;
import static pl.com.bottega.cymes.showscheduler.adapters.db.ShowEntity.SHOWS_BETWEEN_IN_CINEMA;
import static pl.com.bottega.cymes.showscheduler.adapters.db.ShowEntity.SHOWS_BETWEEN_IN_CINEMA_HALL;

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

    @Override
    public Stream<Show> findShowsBetweenInCinema(Instant from, Instant until, Long cinemaId) {
        return springDataShowRepository.findShowsBetweenInCinema(from, until, cinemaId)
            .map(ShowEntity::toShow);
    }

    @Override
    public Stream<Show> findShowsBetweenInCinemaHall(Instant from, Instant until, Long cinemaHallId) {
        return springDataShowRepository.findShowsBetweenInCinemaHall(from, until, cinemaHallId)
            .map(ShowEntity::toShow);
    }

    public long countShows() {
        return springDataShowRepository.count();
    }
}

interface SpringDataShowRepository extends JpaRepository<ShowEntity, UUID> {
        @Query(name = COLLIDING_SHOWS_PRESENT)
        boolean anyShowsCollidingWith(Instant ns, Instant ne, Long cinemaId, Long cinemaHallId);

        @Query(name = SHOWS_BETWEEN_IN_CINEMA)
        Stream<ShowEntity> findShowsBetweenInCinema(Instant fromTime, Instant untilTime, Long cinemaId);

        @Query(name = SHOWS_BETWEEN_IN_CINEMA_HALL)
        Stream<ShowEntity> findShowsBetweenInCinemaHall(Instant fromTime, Instant untilTime, Long cinemaHallId);
        
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
@NamedQuery(
    name = SHOWS_BETWEEN_IN_CINEMA,
    query = "SELECT s FROM Show s WHERE " +
        "((:fromTime <= s.start AND :untilTime >= s.start AND :untilTime <= s.end) OR " +
        "(:fromTime <= s.start AND :untilTime >= s.end)  OR" +
        "(:fromTime >= s.start AND :untilTime <= s.end) OR " +
        "(:fromTime >= s.start AND :fromTime <= s.end AND :untilTime >= s.end)) AND " +
        "s.cinemaId = :cinemaId"
)
@NamedQuery(
    name = SHOWS_BETWEEN_IN_CINEMA_HALL,
    query = "SELECT s FROM Show s WHERE " +
        "((:fromTime <= s.start AND :untilTime >= s.start AND :untilTime <= s.end) OR " +
        "(:fromTime <= s.start AND :untilTime >= s.end)  OR" +
        "(:fromTime >= s.start AND :untilTime <= s.end) OR " +
        "(:fromTime >= s.start AND :fromTime <= s.end AND :untilTime >= s.end)) AND " +
        "s.cinemaHallId = :cinemaHallId"
)
class ShowEntity {

    static final String COLLIDING_SHOWS_PRESENT = "ShowEntity.countCollidingShows";
    static final String SHOWS_BETWEEN_IN_CINEMA = "ShowEntity.showsBetweenInCinema";
    static final String SHOWS_BETWEEN_IN_CINEMA_HALL = "ShowEntity.showsBetweenInCinemaHall";

    @Id
    private UUID id;
    private Long movieId;
    private Long cinemaId;
    private Long cinemaHallId;
    private Instant start;
    private Instant end;
    private boolean canceled;
    @Version
    private Long version;

    ShowEntity(Show show) {
        this.id = show.getId();
        this.movieId = show.getMovieId();
        this.cinemaId = show.getCinemaId();
        this.cinemaHallId = show.getCinemaHallId();
        this.start = show.getStart();
        this.end = show.getEnd();
        this.canceled = show.isCanceled();
        this.version = show.getVersion();
    }

    Show toShow() {
        return new Show(id, movieId, cinemaId, cinemaHallId, start, end, canceled, version);
    }
}
