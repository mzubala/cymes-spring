package pl.com.bottega.cymes.cinemas.services;

import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.com.bottega.cymes.cinemas.services.CinemaService.CinemaCreatedEvent;

@Component
@Log
public class MailingService {

    @EventListener(String.class)
    public void handleStringEvent(String event) {
        log.info("Handling string event = " + event);
    }

    @TransactionalEventListener(CinemaCreatedEvent.class)
    @Async
    public void sendEmailAbout(CinemaCreatedEvent event) throws Exception {
        log.info("Sending email about before = " + event);
        Thread.sleep(5000);
        log.info("Sending email about after = " + event);
    }
}
