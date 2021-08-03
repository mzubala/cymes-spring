package pl.com.bottega.cymes.cinemas.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.resources.request.SuspendRequest;
import pl.com.bottega.cymes.cinemas.resources.request.UpdateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.services.CinemaHallService;
import pl.com.bottega.cymes.cinemas.services.commands.CreateCinemaHallCommand;
import pl.com.bottega.cymes.cinemas.services.commands.SuspendCommand;
import pl.com.bottega.cymes.cinemas.services.commands.UpdateCinemaHallCommand;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionCheckDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
public class CinemaHallResource {

    private final CinemaHallService cinemaHallService;

    @PostMapping
    public void create(@Valid @RequestBody CreateCinemaHallRequest createCinemaHallRequest) {
        var cmd = new CreateCinemaHallCommand();
        cmd.setCinemaId(createCinemaHallRequest.getCinemaId());
        cmd.setName(createCinemaHallRequest.getName());
        cmd.setLayout(createCinemaHallRequest.getLayout());
        cinemaHallService.create(cmd);
    }

    @GetMapping("/{hallId}")
    public DetailedCinemaHallInfoDto get(@PathVariable("hallId") Long cinemaHallId) {
        return cinemaHallService.getCinemaHall(cinemaHallId);
    }

    @PostMapping("/{hallId}/suspensions")
    public void suspend(@PathVariable("hallId") Long hallId, @Valid @RequestBody SuspendRequest suspendRequest) {
        var cmd = new SuspendCommand();
        cmd.setId(hallId);
        cmd.setFrom(suspendRequest.getFrom());
        cmd.setUntil(suspendRequest.getUntil());
        cinemaHallService.suspend(cmd);
    }

    @PutMapping("/{hallId}")
    public void update(@PathVariable("hallId") Long hallId, @Valid @RequestBody UpdateCinemaHallRequest updateCinemaHallRequest) {
        var cmd = new UpdateCinemaHallCommand();
        cmd.setLayout(updateCinemaHallRequest.getLayout());
        cmd.setCinemaHallId(hallId);
        cinemaHallService.updateCinemaHall(cmd);
    }

    @GetMapping("/{hallId}/suspensions")
    public List<SuspensionDto> getSuspensions(@PathVariable("hallId") Long hallId) {
        return cinemaHallService.getSuspensions(hallId);
    }

    @GetMapping("/{hallId}/suspensions/check")
    public SuspensionCheckDto suspensionCheck(@PathVariable("hallId") Long hallId, @RequestParam("from") Instant from, @RequestParam("until") Instant until) {
        return new SuspensionCheckDto(cinemaHallService.isSuspended(hallId, from, until));
    }
}