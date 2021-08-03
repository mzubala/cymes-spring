package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import java.time.Duration;

@ConfigurationProperties(prefix = "show-scheduler")
@Setter
public class SpringShowSchedulerConfiguration implements ShowSchedulerConfiguration {

    private Duration reservationBuffer;

    @Override
    public Duration showReservationBuffer() {
        return reservationBuffer;
    }
}
