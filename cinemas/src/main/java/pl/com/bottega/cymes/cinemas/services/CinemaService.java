package pl.com.bottega.cymes.cinemas.services;

import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaDao;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.SuspensionDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Cinema;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Suspension;
import pl.com.bottega.cymes.cinemas.services.commands.CancelSuspensionCommand;
import pl.com.bottega.cymes.cinemas.services.commands.CreateCinemaCommand;
import pl.com.bottega.cymes.cinemas.services.commands.SuspendCommand;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;
import pl.com.bottega.cymes.cinemas.services.interceptors.Audit;
import pl.com.bottega.cymes.cinemas.services.interceptors.ValidateCommand;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@Audit
@ValidateCommand
@Stateless
public class CinemaService {

    @Inject
    private CinemaDao cinemaDao;

    @Inject
    private CinemaHallService cinemaHallService;

    @Inject
    private SuspensionDao suspensionDao;

    public void create(CreateCinemaCommand cmd) {
        var cinema = new Cinema();
        cinema.setName(cmd.getName());
        cinema.setCity(cmd.getCity());
        cinemaDao.save(cinema);
    }

    public void suspend(SuspendCommand cmd) {
        var cinema = cinemaDao.getReference(cmd.getId());
        var suspension = new Suspension();
        suspension.setCinema(cinema);
        suspension.setFrom(cmd.getFrom());
        suspension.setUntil(cmd.getUntil());
        suspension.setActive(true);
        suspensionDao.save(suspension);
    }

    public void cancelSuspension(CancelSuspensionCommand cmd) {
        var suspension = suspensionDao.find(cmd.getSuspensionId());
        suspension.setActive(false);
    }

    public List<BasicCinemaInfoDto> getBasicCinemaInfo() {
        return cinemaDao.getBasicCinemaInfo();
    }

    public DetailedCinemaInfoDto getDetailedCinemaInfo(Long cinemaId, Instant at) {
        var info = cinemaDao.getDetailedCinemaInfo(cinemaId);
        if(at != null) {
            info.setSuspended(isSuspended(cinemaId, at));
            info.getHalls().forEach((hall) -> hall.setSuspended(cinemaHallService.isSuspended(hall.getId(), at)));
        }
        return info;
    }

    public List<SuspensionDto> getSuspensions(Long cinemaId) {
        return suspensionDao.getActiveCinemaSuspensions(cinemaId);
    }

    public Boolean isSuspended(Long cinemaId, Instant at) {
        return suspensionDao.isCinemaSuspended(cinemaId, at);
    }
}
