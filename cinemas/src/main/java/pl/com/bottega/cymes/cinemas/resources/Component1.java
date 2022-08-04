package pl.com.bottega.cymes.cinemas.resources;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component1 {

    @EventListener(String.class)
    public void handleStringEvent(String event) {
        System.out.println("Hello, I am Component1 and I received " + event);
    }

}
