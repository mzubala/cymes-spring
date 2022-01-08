package pl.com.bottega.cymes.commonstest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class CommonsTestConfiguration {

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    PublishedEventsAssertions publishedEventsAssertions() {
        return new PublishedEventsAssertions();
    }

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    KafkaListenerWaiter kafkaListenerWaiter() {
        return new KafkaListenerWaiter();
    }
}
