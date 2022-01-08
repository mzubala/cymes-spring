package pl.com.bottega.cymes.showscheduler.adapters.listener;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;
import pl.com.bottega.cymes.showscheduler.domain.CinemasEventsHandler;

@Component
@AllArgsConstructor
@Log
public class CinemasListener {

    private final CinemasEventsHandler cinemasEventsHandler;

    @KafkaListener(id = "cancel-shows-on-cinema-suspended", topics = "pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent")
    public void handle(CinemaSuspendedEvent event) {
        log.info("Cinema suspended " + event);
        cinemasEventsHandler.handle(event);
    }

    @KafkaListener(id = "cancel-shows-on-cinema-hall-suspended", topics = "pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent")
    public void handle(CinemaHallSuspendedEvent event) {
        log.info("Cinema hall suspended " + event);
        cinemasEventsHandler.handle(event);
    }
}
