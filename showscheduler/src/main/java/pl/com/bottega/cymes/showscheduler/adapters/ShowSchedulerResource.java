package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowCommand;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowHandler;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowSchedulerResource {


    private final ScheduleShowHandler handler;

    @PostMapping
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
