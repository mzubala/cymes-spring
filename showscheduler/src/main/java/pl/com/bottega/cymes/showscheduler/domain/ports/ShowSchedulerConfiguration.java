package pl.com.bottega.cymes.showscheduler.domain.ports;

import java.time.Duration;

public interface ShowSchedulerConfiguration {
    Duration showReservationBuffer();
}
