package pl.com.bottega.cymes.commonstest;

import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ConsumerStartedEvent;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.awaitility.Awaitility.await;

@Log
public class KafkaListenerWaiter {

    private final List<String> startedConsumerIds = new CopyOnWriteArrayList<>();

    @EventListener
    public void onConsumerStarted(ConsumerStartedEvent consumerStartedEvent) {
        log.info("Consumer started " + consumerStartedEvent.getSource());
        try {
            var source = consumerStartedEvent.getSource(KafkaMessageListenerContainer.class);
            log.info("Added groupId " + source.getGroupId());
            startedConsumerIds.add(source.getGroupId());
        } catch (Exception ex) {

        }
    }

    public void waitUntilConsumerStarted(String groupId) {
        await().until(() -> startedConsumerIds.contains(groupId));
    }

}
