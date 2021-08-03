package pl.com.bottega.cymes.cinemas.dataaccess.dao;

import org.springframework.stereotype.Repository;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Suspension;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;

import java.time.Instant;
import java.util.List;

@Repository
public class SuspensionDao extends GenericDao<Suspension, Long> {
    public List<SuspensionDto> getActiveCinemaSuspensions(Long cinemaId) {
        return entityManager.createNamedQuery("Suspension.getActiveCinemaSuspensions")
            .setParameter("cinemaId", cinemaId).getResultList();
    }

    public Boolean isCinemaSuspended(Long cinemaId, Instant from, Instant until) {
        return entityManager
            .createNamedQuery("Suspension.getActiveCinemaSuspensionAt")
            .setParameter("cinemaId", cinemaId)
            .setParameter("from", from)
            .setParameter("until", until)
            .setMaxResults(1)
            .getResultList().size() > 0;
    }

    public List<SuspensionDto> getActiveCinemaHallSuspensions(Long cinemaId) {
        return entityManager.createNamedQuery("Suspension.getActiveCinemaHallSuspensions")
            .setParameter("cinemaHallId", cinemaId).getResultList();
    }

    public Boolean isCinemaHallSuspended(Long cinemaId, Instant at) {
        return entityManager
            .createNamedQuery("Suspension.getActiveCinemaHallSuspensionAt")
            .setParameter("cinemaHallId", cinemaId)
            .setParameter("at", at)
            .setMaxResults(1)
            .getResultList().size() > 0;
    }
}
