package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Date;

public interface CinemasClient {
    @GET
    @Path("/cinemas/{id}/suspensions")
    @Consumes("application/json")
    SuspensionCheck checkCinemaSuspension(@PathParam("id") Long cinemaId, @QueryParam("from") Date from, @QueryParam("until") Date until);

    @GET
    @Path("/halls/{id}/suspensions")
    @Consumes("application/json")
    SuspensionCheck checkCinemaHallSuspension(@PathParam("id") Long cinemaHallId, @QueryParam("from") Date from, @QueryParam("until") Date until);

    @Data
    class SuspensionCheck {
        private Boolean suspended;
    }
}
