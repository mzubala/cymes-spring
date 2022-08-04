package pl.com.bottega.cymes.cinemas.resources;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PeriodicTasksController {

    private ApplicationEventPublisher publisher;

    @Scheduled(fixedRate = 5000)
    public void doSthFromTimeToTime() {
        System.out.println("Hello scheduled!");
        publisher.publishEvent("this is my event");
        publisher.publishEvent(new SampleEvent("this is my payload"));
    }
}

@Value
class SampleEvent {
    String payload;
}
