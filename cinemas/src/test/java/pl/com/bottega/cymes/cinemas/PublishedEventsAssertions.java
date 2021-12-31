package pl.com.bottega.cymes.cinemas;

import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.awaitility.Awaitility.await;

@Component
@Log
public class PublishedEventsAssertions {

    private final List<Object> events = new CopyOnWriteArrayList<>();

    @KafkaListener(
        groupId = "cymes-test",
        topicPattern = "pl\\.com\\.bottega\\.cymes.*"
    )
    public void listen(ConsumerRecord<?, ?> event) {
        log.info("Event received " + event);
        events.add(event.value());
    }

    public void awaitEventPublished(Object event) {
        await().until(() -> events.contains(event));
    }
}
