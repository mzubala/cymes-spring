package pl.com.bottega.cymes.showscheduler.adapters;

import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import java.time.Duration;

public class SpringShowSchedulerConfiguration implements ShowSchedulerConfiguration {

    @Override
    public Duration showReservationBuffer() {
        return null;
    }
}
