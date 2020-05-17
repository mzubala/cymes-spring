package pl.com.bottega.cymes.cinemas.resources;

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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@Path("/halls")
@Consumes({"application/json"})
@Produces({"application/json"})
public class CinemaHallResource {

    @Inject
    private CinemaHallService cinemaHallService;

    @POST
    public void create(CreateCinemaHallRequest createCinemaHallRequest) {
        var cmd = new CreateCinemaHallCommand();
        cmd.setCinemaId(createCinemaHallRequest.getCinemaId());
        cmd.setName(createCinemaHallRequest.getName());
        cmd.setLayout(createCinemaHallRequest.getLayout());
        cinemaHallService.create(cmd);
    }

    @GET
    @Path("/{hallId}")
    public DetailedCinemaHallInfoDto get(@PathParam("hallId") Long cinemaHallId) {
        return cinemaHallService.getCinemaHall(cinemaHallId);
    }

    @POST
    @Path("/{hallId}/suspensions")
    public void suspend(@PathParam("hallId") Long hallId, SuspendRequest suspendRequest) {
        var cmd = new SuspendCommand();
        cmd.setId(hallId);
        cmd.setFrom(suspendRequest.getFrom());
        cmd.setUntil(suspendRequest.getUntil());
        cinemaHallService.suspend(cmd);
    }

    @PUT
    @Path("/{hallId}")
    public void update(@PathParam("hallId") Long hallId, UpdateCinemaHallRequest updateCinemaHallRequest) {
        var cmd = new UpdateCinemaHallCommand();
        cmd.setLayout(updateCinemaHallRequest.getLayout());
        cmd.setCinemaHallId(hallId);
        cinemaHallService.updateCinemaHall(cmd);
    }

    @GET
    @Path("/{hallId}/suspensions")
    public List<SuspensionDto> getSuspensions(@PathParam("hallId") Long hallId) {
        return cinemaHallService.getSuspensions(hallId);
    }

    @GET
    @Path("/{hallId}/suspensions/check")
    public SuspensionCheckDto suspensionCheck(@PathParam("hallId") Long hallId, @QueryParam("from") Date from, @QueryParam("until") Date until) {
        return new SuspensionCheckDto(cinemaHallService.isSuspended(hallId, from.toInstant(), until.toInstant()));
    }
}