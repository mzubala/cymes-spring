package pl.com.bottega.cymes.cinemas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaDao;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.SuspensionDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Cinema;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Suspension;
import pl.com.bottega.cymes.cinemas.services.audit.Audit;
import pl.com.bottega.cymes.cinemas.services.commands.CancelSuspensionCommand;
import pl.com.bottega.cymes.cinemas.services.commands.CreateCinemaCommand;
import pl.com.bottega.cymes.cinemas.services.commands.SuspendCommand;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Audit
public class CinemaService {

    private final CinemaDao cinemaDao;

    private final CinemaHallService cinemaHallService;

    private final SuspensionDao suspensionDao;

    @Transactional
    public void create(CreateCinemaCommand cmd) {
        var cinema = new Cinema();
        cinema.setName(cmd.getName());
        cinema.setCity(cmd.getCity());
        cinemaDao.save(cinema);
    }

    @Transactional
    public void suspend(SuspendCommand cmd) {
        var cinema = cinemaDao.getReference(cmd.getId());
        var suspension = new Suspension();
        suspension.setCinema(cinema);
        suspension.setFrom(cmd.getFrom());
        suspension.setUntil(cmd.getUntil());
        suspension.setActive(true);
        suspensionDao.save(suspension);
    }

    @Transactional
    public void cancelSuspension(CancelSuspensionCommand cmd) {
        var suspension = suspensionDao.find(cmd.getSuspensionId());
        suspension.setActive(false);
    }

    @Transactional(readOnly = true)
    public List<BasicCinemaInfoDto> getBasicCinemaInfo() {
        return cinemaDao.getBasicCinemaInfo();
    }

    @Transactional(readOnly = true)
    public DetailedCinemaInfoDto getDetailedCinemaInfo(Long cinemaId, Instant at) {
        var info = cinemaDao.getDetailedCinemaInfo(cinemaId);
        if(at != null) {
            info.setSuspended(isSuspended(cinemaId, at, at));
            info.getHalls().forEach((hall) -> hall.setSuspended(cinemaHallService.isSuspended(hall.getId(), at, at)));
        }
        return info;
    }

    @Transactional(readOnly = true)
    public List<SuspensionDto> getSuspensions(Long cinemaId) {
        return suspensionDao.getActiveCinemaSuspensions(cinemaId);
    }

    @Transactional(readOnly = true)
    public Boolean isSuspended(Long cinemaId, Instant from, Instant until) {
        return suspensionDao.isCinemaSuspended(cinemaId, from, until);
    }
}
