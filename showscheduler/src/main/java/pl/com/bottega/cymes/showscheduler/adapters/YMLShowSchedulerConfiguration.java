package pl.com.bottega.cymes.showscheduler.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.Duration;

@Dependent
public class YMLShowSchedulerConfiguration implements ShowSchedulerConfiguration {

    private static final String YAML_PATH = "/application.yml";

    @Inject
    private ObjectMapper objectMapper;

    private ShowSchedulerProps props;

    @PostConstruct
    @SneakyThrows
    public void init() {
        props = objectMapper.readValue(this.getClass().getResourceAsStream(YAML_PATH), ShowSchedulerProps.class);
    }

    @Override
    public Duration showReservationBuffer() {
        return props.reservationBuffer;
    }

    public String cinemasUrl() {
        return props.cinemasUrl;
    }

    public String moviesUrl() {
        return props.moviesUrl;
    }

    @Data
    public static class ShowSchedulerProps {
        private Duration reservationBuffer;
        private String cinemasUrl;
        private String moviesUrl;
    }
}
