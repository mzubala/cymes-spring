package pl.com.bottega.cymes.showscheduler.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;

public class YMLShowSchedulerConfiguration implements ShowSchedulerConfiguration {

    private static final String YAML_PATH = "/application.yml";

    @Inject
    private ObjectMapper objectMapper;

    private ShowSchedulerProps props;

    @PostConstruct
    public void init() throws IOException {
        props = objectMapper.readValue(this.getClass().getResourceAsStream(YAML_PATH), ShowSchedulerProps.class);
    }

    @Override
    public Duration showReservationBuffer() {
        return props.reservationBuffer;
    }

    public String cinemasUrl() {
        return props.cinemasUrl;
    }

    @Data
    public static class ShowSchedulerProps {
        private Duration reservationBuffer;
        private String cinemasUrl;
    }
}
