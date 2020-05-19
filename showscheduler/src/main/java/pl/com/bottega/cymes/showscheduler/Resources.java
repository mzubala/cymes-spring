package pl.com.bottega.cymes.showscheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.com.bottega.cymes.showscheduler.adapters.JPAOperationLocker;
import pl.com.bottega.cymes.showscheduler.domain.DefaultScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.ShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public ScheduleShowHandler scheduleShowCommand(
        MovieCatalog movieCatalog, ShowRepository showRepository, SuspensionChecker suspensionChecker,
        JPAOperationLocker operationLocker, ShowSchedulerConfiguration configuration) {
        return new DefaultScheduleShowHandler(movieCatalog, showRepository, suspensionChecker, operationLocker, configuration);
    }
}
