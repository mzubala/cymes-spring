package pl.com.bottega.cymes.showscheduler.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import javax.inject.Inject;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(Arquillian.class)
public class YMLShowSchedulerConfigurationTest {

    @Inject
    private ShowSchedulerConfiguration showSchedulerConfiguration;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Test
    public void returnsReservationBuffer() {
        assertThat(showSchedulerConfiguration.showReservationBuffer()).isEqualTo(Duration.ofMinutes(45));
    }
}
