package pl.com.bottega.cymes.showscheduler.adapters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.cymes.showscheduler.domain.DefaultScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;
import pl.com.bottega.cymes.showscheduler.domain.OperationLocker;
import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.ShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ShowSchedulerConfiguration;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;

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
