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
        showRepository
            .findShowsBetweenInCinema(event.getFrom(), event.getUntil(), event.getCinemaId())
            .forEach(this::cancelAndSave);
    }

    @Transactional
    public void handle(CinemaHallSuspendedEvent event) {
        showRepository
            .findShowsBetweenInCinemaHall(event.getFrom(), event.getUntil(), event.getCinemaHallId())
            .forEach(this::cancelAndSave);
    }

    private void cancelAndSave(Show show) {
        show.cancel();
        showRepository.save(show);
    }
}
