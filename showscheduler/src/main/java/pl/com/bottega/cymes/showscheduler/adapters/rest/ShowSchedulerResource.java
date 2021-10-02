package pl.com.bottega.cymes.showscheduler.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.showscheduler.adapters.security.UserProvider;
import pl.com.bottega.cymes.showscheduler.domain.CinemaHallNotAvailableException;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowCommand;
import pl.com.bottega.cymes.showscheduler.domain.ports.ScheduleShowHandler;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowSchedulerResource {

    private static final String CONSTRAINT_VIOLATION = "23505";
    private final ScheduleShowHandler handler;

    private final UserProvider userProvider;

    @PostMapping
    public void scheduleShow(@RequestBody ScheduleShowRequest request) {
        var command= request.toCommand(userProvider.currentUserId());
        handler.handle(command);
    }

    @ExceptionHandler(CinemaHallNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleCinemaHallNotAvailableException() {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleShowAlreadyScheduledException(PSQLException psqlException) throws PSQLException {
        if(psqlException.getServerErrorMessage().getSQLState().equals(CONSTRAINT_VIOLATION)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        throw psqlException;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleShowRequest {
        private String showId;
        private Long movieId;
        private Long cinemaId;
        private Long cinemaHallId;
        private Instant when;

        ScheduleShowCommand toCommand(Long userId) {
            return ScheduleShowCommand.builder()
                    .cinemaHallId(cinemaHallId)
                    .cinemaId(cinemaId)
                    .movieId(movieId)
                    .userId(1L)
                    .showId(UUID.fromString(showId))
                    .when(when)
                    .userId(userId)
                    .build();
        }
    }
}
