package pl.com.bottega.cymes.showscheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.com.bottega.cymes.showscheduler.domain.*;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class Resources {

    @PersistenceContext
    @Produces
    private EntityManager em;

    @Produces
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Produces
    public ScheduleShowHandler scheduleShowHandler(
            MovieCatalog movieCatalog, ShowRepository showRepository,
            SuspensionChecker suspensionChecker, OperationLocker operationLocker, ShowSchedulerConfiguration showSchedulerConfiguration
    ) {
        return new DefaultScheduleShowHandler(movieCatalog, showRepository, suspensionChecker, operationLocker, showSchedulerConfiguration);
    }
}
