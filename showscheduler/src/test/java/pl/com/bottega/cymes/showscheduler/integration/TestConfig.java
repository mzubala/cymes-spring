package pl.com.bottega.cymes.showscheduler.integration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;

@Configuration
public class TestConfig {
    @Bean
    NewTopic cinemaSuspendedEventTopic() {
        return new NewTopic(CinemaSuspendedEvent.class.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic cinemaHallSuspendedEventTopic() {
        return new NewTopic(CinemaHallSuspendedEvent.class.getName(), 1, (short) 1);
    }
}
