package pl.com.bottega.cymes.showscheduler.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import javax.inject.Inject;
import java.time.Duration;

public class YMLShowSchedulerConfiguration implements ShowSchedulerConfiguration {

    private static final String YAML_PATH = "/application.yml";

    @Inject
    private ObjectMapper objectMapper;



    @Override
    public Duration showReservationBuffer() {
        return null;
    }
}
