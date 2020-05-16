package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

public class JPAShowRepository implements ShowRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Show show) {

    }

    @Override
    public boolean anyShowsCollidingWith(Show show) {
        return false;
    }

    @Override
    public Show get(UUID showId) {
        return null;
    }

    @Entity(name = "Show")
    @Table(name = "shows")
    @NoArgsConstructor
    public static class ShowEntity {

        static final String COUNT_COLLIDING_SHOWS = "ShowEntity.countCollidingShows";

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
}
