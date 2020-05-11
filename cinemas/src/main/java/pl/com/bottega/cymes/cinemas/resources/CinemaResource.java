package pl.com.bottega.cymes.cinemas.resources;

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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@Path("/cinemas")
@Consumes({"application/json"})
@Produces({"application/json"})
public class CinemaResource {

    @Inject
    private CinemaService cinemaService;

    @POST
    public void create(CreateCinemaRequest createCinemaRequest) {
        var cmd = new CreateCinemaCommand();
        cmd.setName(createCinemaRequest.getName());
        cmd.setCity(createCinemaRequest.getCity());
        cinemaService.create(cmd);
    }

    @GET
    public List<BasicCinemaInfoDto> getAll() {
        return cinemaService.getBasicCinemaInfo();
    }

    @POST
    @Path("/{id}/suspensions")
    public void suspend(@PathParam("id") Long cinemaId, SuspendRequest suspendRequest) {
        var cmd = new SuspendCommand();
        cmd.setId(cinemaId);
        cmd.setUntil(suspendRequest.getUntil());
        cmd.setFrom(suspendRequest.getFrom());
        cinemaService.suspend(cmd);
    }

    @DELETE
    @Path("/suspensions/{id}")
    public void cancelSuspension(@PathParam("id") Long suspensionId) {
        var cmd = new CancelSuspensionCommand();
        cmd.setSuspensionId(suspensionId);
        cinemaService.cancelSuspension(cmd);
    }

    @GET
    @Path("/{cinemaId}/suspensions")
    public List<SuspensionDto> getSuspensions(@PathParam("cinemaId") Long cinemaId) {
        return cinemaService.getSuspensions(cinemaId);
    }

    @GET
    @Path("/{cinemaId}/suspensions/check")
    public SuspensionCheckDto suspensionCheck(@PathParam("cinemaId") Long cinemaId, @QueryParam("at") Date at) {
        return new SuspensionCheckDto(cinemaService.isSuspended(cinemaId, at.toInstant()));
    }

    @GET
    @Path("/{id}")
    public DetailedCinemaInfoDto get(@PathParam("id") Long cinemaId, @QueryParam("at") Date at) {
        return cinemaService.getDetailedCinemaInfo(cinemaId, at == null ? null : at.toInstant());
    }
}

