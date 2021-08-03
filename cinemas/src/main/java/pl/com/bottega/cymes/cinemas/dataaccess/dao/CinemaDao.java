package pl.com.bottega.cymes.cinemas.dataaccess.dao;

import org.springframework.stereotype.Repository;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Cinema;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto;

import java.util.List;

@Repository
public class CinemaDao extends GenericDao<Cinema, Long> {
    public List<BasicCinemaInfoDto> getBasicCinemaInfo() {
        return entityManager.createNamedQuery("Cinema.getBasicCinemaInfo").getResultList();
    }

    public DetailedCinemaInfoDto getDetailedCinemaInfo(Long cinemaId) {
        var cinemaInfo = entityManager.createNamedQuery("Cinema.getDetailedCinemaInfo", DetailedCinemaInfoDto.class)
            .setParameter("cinemaId", cinemaId)
            .getSingleResult();
        cinemaInfo.setHalls(
            entityManager.createNamedQuery("CinemaHall.getBasicInfo").setParameter("cinemaId", cinemaId).getResultList()
        );
        return cinemaInfo;
    }
}
