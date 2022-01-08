package pl.com.bottega.cymes.showscheduler.integration;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;
import pl.com.bottega.cymes.commonstest.KafkaListenerWaiter;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@IntegrationTest
@Log
public class CinemaListenerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private KafkaListenerWaiter kafkaListenerWaiter;

    @Test
    public void cancelsShowWhenCinemaIsSuspended() {
        // given
        var show = new ShowExample().toShow();
        showRepository.save(show);
        kafkaListenerWaiter.waitUntilConsumerStarted("cancel-shows-on-cinema-suspended");

        // when
        kafkaTemplate.send(CinemaSuspendedEvent.class.getName(), new CinemaSuspendedEvent(show.getCinemaId(), show.getStart(), show.getEnd()));

        // then
        await().untilAsserted(() -> assertThat(showRepository.get(show.getId()).isCanceled()).isTrue());
    }

    @Test
    public void cancelsShowWhenCinemaHallIsSuspended() {
        // given
        var show = new ShowExample().toShow();
        showRepository.save(show);
        kafkaListenerWaiter.waitUntilConsumerStarted("cancel-shows-on-cinema-hall-suspended");

        // when
        kafkaTemplate.send(
            CinemaHallSuspendedEvent.class.getName(),
            new CinemaHallSuspendedEvent(show.getCinemaHallId(), show.getStart(), show.getEnd())
        );

        // then
        await().untilAsserted(() -> assertThat(showRepository.get(show.getId()).isCanceled()).isTrue());
    }
}
