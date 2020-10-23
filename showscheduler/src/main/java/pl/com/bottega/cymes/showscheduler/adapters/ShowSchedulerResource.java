package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;
import lombok.NonNull;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowCommand;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowHandler;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.Instant;
import java.util.UUID;

@Path("/shows")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ShowSchedulerResource {

    @Inject
    private ScheduleShowHandler handler;

    @POST
    public void scheduleShow(ScheduleShowRequest request) {
        var command = request.toCommand();
        handler.handle(command);
    }

    @Data
    static class ScheduleShowRequest {
        @NonNull
        private final String showId;
        @NonNull
        private final Long movieId;
        @NonNull
        private final Long cinemaId;
        @NonNull
        private final Long cinemaHallId;
        @NonNull
        private final Instant when;

        ScheduleShowCommand toCommand() {
            return ScheduleShowCommand.builder()
                    .cinemaHallId(cinemaHallId)
                    .cinemaId(cinemaId)
                    .movieId(movieId)
                    .userId(1L)
                    .showId(UUID.fromString(showId))
                    .when(when)
                    .build();
        }
    }
}
