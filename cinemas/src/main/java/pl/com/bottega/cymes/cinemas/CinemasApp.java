package pl.com.bottega.cymes.cinemas;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;

@SpringBootApplication
public class CinemasApp {
    public static void main(String[] args) {
        SpringApplication.run(CinemasApp.class, args);
    }

    @Bean
    NewTopic cinemaSuspendedEventTopic() {
        return new NewTopic(CinemaSuspendedEvent.class.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic cinemaHallSuspendedEventTopic() {
        return new NewTopic(CinemaHallSuspendedEvent.class.getName(), 1, (short) 1);
    }
}
