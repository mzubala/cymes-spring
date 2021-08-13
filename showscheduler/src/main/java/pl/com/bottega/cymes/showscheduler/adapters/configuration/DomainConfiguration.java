package pl.com.bottega.cymes.showscheduler.adapters.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.cymes.showscheduler.domain.DefaultScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.ports.MovieCatalog;
import pl.com.bottega.cymes.showscheduler.domain.ports.OperationLocker;
import pl.com.bottega.cymes.showscheduler.domain.ports.ScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowSchedulerConfiguration;
import pl.com.bottega.cymes.showscheduler.domain.ports.SuspensionChecker;

@Configuration
public class DomainConfiguration {
    @Bean
    public ScheduleShowHandler scheduleShowHandler(MovieCatalog movieCatalog,
                                                   ShowRepository showRepository,
                                                   SuspensionChecker suspensionChecker,
                                                   OperationLocker operationLocker,
                                                   ShowSchedulerConfiguration showSchedulerConfiguration
    ) {
        return new DefaultScheduleShowHandler(movieCatalog, showRepository, suspensionChecker, operationLocker, showSchedulerConfiguration);
    }
}
