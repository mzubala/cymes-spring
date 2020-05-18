package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface MoviesClient {
    @GET
    @Path("/movies/{id}")
    @Consumes("application/json")
    MovieJson getMovie(@PathParam("id") Long movieId);

    @Data
    class MovieJson {
        private Long id;
        private Integer durationMinutes;
    }
}


