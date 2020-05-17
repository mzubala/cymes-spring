package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

import static javax.transaction.Transactional.TxType.SUPPORTS;

public class JPAShowRepository implements ShowRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Show show) {
        entityManager.merge(new ShowEntity(show));
    }

    @Override
    @Transactional(SUPPORTS)
    public boolean anyShowsCollidingWith(Show show) {
        return entityManager.createNamedQuery(ShowEntity.COUNT_COLLIDING_SHOWS, Long.class)
            .setParameter("ns", show.getStart())
            .setParameter("ne", show.getEnd())
            .getSingleResult() > 0;
    }

    @Override
    public Show get(UUID showId) {
        var entity = entityManager.find(ShowEntity.class, showId);
        if(entity == null) {
            throw new ShowNotFoundException();
        }
        return entity.toShow();
    }

    @Entity(name = "Show")
    @Table(name = "shows")
    @NamedQuery(
        name = ShowEntity.COUNT_COLLIDING_SHOWS,
        query = "SELECT count(s) FROM Show s WHERE " +
            "(:ns <= s.start AND :ne >= s.start AND :ne <= s.end) OR " +
            "(:ns <= s.start AND :ne >= s.end)  OR" +
            "(:ns >= s.start AND :ne <= s.end) OR " +
            "(:ns >= s.start AND :ns <= s.end AND :ne >= s.end)"
    )
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