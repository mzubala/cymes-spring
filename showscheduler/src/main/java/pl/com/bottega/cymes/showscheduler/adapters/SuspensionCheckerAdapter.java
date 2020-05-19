package pl.com.bottega.cymes.showscheduler.adapters;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Date;

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
        var cmd1 = new CheckCinemaHallSuspensionCommand(show);
        var cmd2 = new CheckCinemaSuspensionCommand(show);
        return cmd1.observe().zipWith(cmd2.observe(), (b1, b2) -> b1 || b2).subscribeOn(Schedulers.computation()).toBlocking().single();
    }

    class CheckCinemaSuspensionCommand extends HystrixCommand<Boolean> {

        private Show show;

        public CheckCinemaSuspensionCommand(Show show) {
            super(HystrixCommandGroupKey.Factory.asKey("CinemasGroup"));
            this.show = show;
        }

        @Override
        protected Boolean run() throws Exception {
            return cinemasClient.checkCinemaSuspension(show.getCinemaId(), Date.from(show.getStart()), Date.from(show.getEnd())).getSuspended();
        }
    }

    class CheckCinemaHallSuspensionCommand extends HystrixCommand<Boolean> {

        private Show show;

        public CheckCinemaHallSuspensionCommand(Show show) {
            super(HystrixCommandGroupKey.Factory.asKey("CinemasGroup"));
            this.show = show;
        }

        @Override
        protected Boolean run() throws Exception {
            return cinemasClient.checkCinemaHallSuspension(show.getCinemaHallId(), Date.from(show.getStart()), Date.from(show.getEnd())).getSuspended();
        }
    }

}
