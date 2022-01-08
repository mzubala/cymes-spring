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

    // TODO listen to cinema events and use handler
}
