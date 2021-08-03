package pl.com.bottega.cymes.cinemas.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.resources.request.SuspendRequest;
import pl.com.bottega.cymes.cinemas.services.CinemaService;
import pl.com.bottega.cymes.cinemas.services.commands.CancelSuspensionCommand;
import pl.com.bottega.cymes.cinemas.services.commands.CreateCinemaCommand;
import pl.com.bottega.cymes.cinemas.services.commands.SuspendCommand;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionCheckDto;
import pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequestMapping("/cinemas")
@RestController
@RequiredArgsConstructor
public class CinemaResource {

    private final CinemaService cinemaService;

    @PostMapping
    public void create(@Valid @RequestBody CreateCinemaRequest createCinemaRequest) {
        var cmd = new CreateCinemaCommand();
        cmd.setName(createCinemaRequest.getName());
        cmd.setCity(createCinemaRequest.getCity());
        cinemaService.create(cmd);
    }

    @GetMapping
    public List<BasicCinemaInfoDto> getAll() {
        return cinemaService.getBasicCinemaInfo();
    }

    @PostMapping("/{id}/suspensions")
    public void suspend(@PathVariable("id") Long cinemaId, @Valid @RequestBody SuspendRequest suspendRequest) {
        var cmd = new SuspendCommand();
        cmd.setId(cinemaId);
        cmd.setUntil(suspendRequest.getUntil());
        cmd.setFrom(suspendRequest.getFrom());
        cinemaService.suspend(cmd);
    }

    @DeleteMapping("/suspensions/{id}")
    public void cancelSuspension(@PathVariable("id") Long suspensionId) {
        var cmd = new CancelSuspensionCommand();
        cmd.setSuspensionId(suspensionId);
        cinemaService.cancelSuspension(cmd);
    }

    @GetMapping("/{cinemaId}/suspensions")
    public List<SuspensionDto> getSuspensions(@PathVariable("cinemaId") Long cinemaId) {
        return cinemaService.getSuspensions(cinemaId);
    }

    @GetMapping("/{cinemaId}/suspensions/check")
    public SuspensionCheckDto suspensionCheck(@PathVariable("cinemaId") Long cinemaId, @RequestParam("from") Instant from, @RequestParam("until") Instant until) {
        return new SuspensionCheckDto(cinemaService.isSuspended(cinemaId, from, until));
    }

    @GetMapping("/{id}")
    public DetailedCinemaInfoDto get(@PathVariable("id") Long cinemaId, @RequestParam("at") Date at) {
        return cinemaService.getDetailedCinemaInfo(cinemaId, at == null ? null : at.toInstant());
    }
}

