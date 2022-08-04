package pl.com.bottega.cymes.cinemas.resources;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Component2 {

    @EventListener(String.class)
    @Async
    public void handleStringEvent(String event) {
        System.out.println("Hello, I am Component2 and I received " + event);
    }

    @EventListener(SampleEvent.class)
    @Async
    public void handleSampleEvent(SampleEvent event) {
        System.out.println("Hello, I am Component2 and I received " + event);
    }

}
