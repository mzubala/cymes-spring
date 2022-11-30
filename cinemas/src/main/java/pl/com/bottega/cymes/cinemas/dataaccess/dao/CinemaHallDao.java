package pl.com.bottega.cymes.cinemas.dataaccess.dao;

import org.springframework.stereotype.Repository;
import pl.com.bottega.cymes.cinemas.dataaccess.model.CinemaHall;

@Repository
public class CinemaHallDao extends GenericDao<CinemaHall, Long> {
    public CinemaHall findWithLayout(Long id) {
        return entityManager.createNamedQuery("CinemaHall.getWithLayout", CinemaHall.class)
                .setParameter("cinemaHallId", id)
                .getResultStream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException(CinemaHall.class, id));
    }
}
