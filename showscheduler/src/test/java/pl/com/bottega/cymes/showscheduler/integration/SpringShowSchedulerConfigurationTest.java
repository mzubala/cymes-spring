package pl.com.bottega.cymes.showscheduler.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowSchedulerConfiguration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class SpringShowSchedulerConfigurationTest {
    @Autowired
    private ShowSchedulerConfiguration configuration;

    @Test
    public void returnsConfigurationParams() {
        assertThat(configuration.showReservationBuffer()).isEqualTo(Duration.ofMinutes(1));
    }
}
