package pl.com.bottega.cymes.cinemas.services;

import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log
public class TestListenerService {

    @EventListener(classes = String.class)
    public void handleStringEvent(String event) {
        log.info("Handling string event = " + event);
    }

    @EventListener(classes = Long.class)
    public void handleLongEvent(Long event) {
        log.info("Handling long event = " + event);
    }

}
