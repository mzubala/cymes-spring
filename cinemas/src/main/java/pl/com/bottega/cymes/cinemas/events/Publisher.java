package pl.com.bottega.cymes.cinemas.events;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Publisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishSuspension(Object event) {

    }

}
