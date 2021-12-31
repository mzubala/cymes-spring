package pl.com.bottega.cymes.cinemas.events;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class Publisher {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(
        classes = {
            CinemaSuspendedEvent.class,
            CinemaHallSuspendedEvent.class
        }
    )
    public void publishSuspension(Object event) {
        kafkaTemplate.send(event.getClass().getName(), event);
    }

}
