package pl.com.bottega.cymes.showscheduler.adapters;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowCommand;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowHandler;

import javax.inject.Inject;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Path("/shows")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ShowResource {

    @Inject
    private ScheduleShowHandler scheduleShowHandler;

    @Inject
    private UserProvider userProvider;

    @POST
    public void schedule(ScheduleShowRequest request) {
        scheduleShowHandler.handle(request.toCommand(userProvider));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleShowRequest {
        private UUID id;
        private Long movieId;
        private Long cinemaId;
        private Long cinemaHallId;
        @JsonbDateFormat("dd/MM/yyyy HH:mm")
        private Date start;

        ScheduleShowCommand toCommand(UserProvider userProvider) {
            return new ScheduleShowCommand(id, userProvider.currentUserId(), movieId, cinemaId, cinemaHallId, start.toInstant());
        }
    }
}
