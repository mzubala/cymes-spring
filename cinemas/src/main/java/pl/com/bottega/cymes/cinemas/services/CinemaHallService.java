package pl.com.bottega.cymes.cinemas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaDao;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaHallDao;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.SuspensionDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.CinemaHall;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Row;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElement;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Suspension;
import pl.com.bottega.cymes.cinemas.services.audit.Audit;
import pl.com.bottega.cymes.cinemas.services.commands.CreateCinemaHallCommand;
import pl.com.bottega.cymes.cinemas.services.commands.SuspendCommand;
import pl.com.bottega.cymes.cinemas.services.commands.UpdateCinemaHallCommand;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;
import pl.com.bottega.cymes.cinemas.services.dto.RowElementDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Audit
@RequiredArgsConstructor
public class CinemaHallService {

    private final CinemaDao cinemaDao;

    private final CinemaHallDao cinemaHallDao;

    private final SuspensionDao suspensionDao;

    @Transactional
    public void create(CreateCinemaHallCommand cmd) {
        var cinema = cinemaDao.getReference(cmd.getCinemaId());
        var hall = new CinemaHall();
        hall.setCinema(cinema);
        hall.setName(cmd.getName());
        hall.setRows(cmd.getLayout().stream().map(this::toRow).collect(toList()));
        hall.setCapacity(calculateCapacity(hall));
        cinemaHallDao.save(hall);
    }

    @Transactional
    public void updateCinemaHall(UpdateCinemaHallCommand cmd) {
        var hall = cinemaHallDao.find(cmd.getCinemaHallId());
        hall.setRows(cmd.getLayout().stream().map(this::toRow).collect(toList()));
    }

    @Transactional
    public void suspend(SuspendCommand cmd) {
        var suspension = new Suspension();
        suspension.setFrom(cmd.getFrom());
        suspension.setUntil(cmd.getUntil());
        suspension.setCinemaHall(cinemaHallDao.getReference(cmd.getId()));
        suspensionDao.save(suspension);
    }

    @Transactional(readOnly = true)
    public DetailedCinemaHallInfoDto getCinemaHall(Long id) {
        var cinemaHall = cinemaHallDao.find(id);
        var result = new DetailedCinemaHallInfoDto();
        result.setCapacity(cinemaHall.getCapacity());
        result.setId(cinemaHall.getId());
        result.setName(cinemaHall.getName());
        result.setRows(cinemaHall.getRows().stream().map(this::toDto).collect(toList()));
        return result;
    }

    private RowDto toDto(Row row) {
        var dto = new RowDto();
        dto.setNumber(row.getNumber());
        dto.setElements(row.getElements().stream().map(this::toDot).collect(toList()));
        return dto;
    }

    private RowElementDto toDot(RowElement rowElement) {
        var dto = new RowElementDto();
        dto.setIndex(rowElement.getIndex());
        dto.setKind(rowElement.getElementKind());
        dto.setNumber(rowElement.getNumber());
        return dto;
    }

    private Integer calculateCapacity(CinemaHall hall) {
        return hall.getRows().stream()
            .map((row) -> row.getElements().size())
            .reduce(Integer::sum)
            .orElse(0);
    }

    private Row toRow(RowDto rowDto) {
        var row = new Row();
        row.setElements(rowDto.getElements().stream().map(this::toRowElement).collect(toList()));
        return row;
    }

    private RowElement toRowElement(RowElementDto rowElementDto) {
        var element = new RowElement();
        element.setElementKind(rowElementDto.getKind());
        element.setIndex(rowElementDto.getIndex());
        element.setNumber(rowElementDto.getNumber());
        return element;
    }

    public boolean isSuspended(Long hallId, Instant from, Instant until) {
        return false;
    }

    public List<SuspensionDto> getSuspensions(Long hallId) {
        return null;
    }
}
