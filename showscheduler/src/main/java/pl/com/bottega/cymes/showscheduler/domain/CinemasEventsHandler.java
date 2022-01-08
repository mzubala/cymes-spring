package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;

@AllArgsConstructor
public class CinemasEventsHandler {

    private final ShowRepository showRepository;

    @Transactional
    public void handle(CinemaSuspendedEvent event) {
        // TODO implement event handling
    }

    @Transactional
    public void handle(CinemaHallSuspendedEvent event) {
        // TODO implement event handling
    }

}
