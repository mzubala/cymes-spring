package pl.com.bottega.cymes.showscheduler.adapters;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Date;

@Dependent
public class SuspensionCheckerAdapter implements SuspensionChecker {

    @Inject
    private YMLShowSchedulerConfiguration configuration;

    private CinemasClient cinemasClient;

    @PostConstruct
    public void init() {
        cinemasClient = new ResteasyClientBuilder().build().target(configuration.cinemasUrl()).proxy(CinemasClient.class);
    }

    @Override
    public boolean anySuspensionsAtTimeOf(Show show) {
        var cinemaSuspensionCheck = cinemasClient.checkCinemaSuspension(show.getCinemaId(), Date.from(show.getStart()), Date.from(show.getEnd()));
        var cinemaHallSuspensionCheck = cinemasClient.checkCinemaHallSuspension(show.getCinemaHallId(), Date.from(show.getStart()), Date.from(show.getEnd()));
        return cinemaHallSuspensionCheck.getSuspended() || cinemaSuspensionCheck.getSuspended();
    }
}
