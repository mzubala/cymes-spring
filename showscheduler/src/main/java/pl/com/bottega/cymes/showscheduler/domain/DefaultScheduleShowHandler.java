package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import pl.com.bottega.cymes.showscheduler.domain.ports.MovieCatalog;
import pl.com.bottega.cymes.showscheduler.domain.ports.OperationLocker;
import pl.com.bottega.cymes.showscheduler.domain.ports.ScheduleShowHandler;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowSchedulerConfiguration;
import pl.com.bottega.cymes.showscheduler.domain.ports.SuspensionChecker;

@AllArgsConstructor
public class DefaultScheduleShowHandler implements ScheduleShowHandler {

    @NonNull
    private final MovieCatalog movieCatalog;

    @NonNull
    private final ShowRepository showRepository;

    @NonNull
    private final SuspensionChecker suspensionChecker;

    @NonNull
    private final OperationLocker operationLocker;

    @NonNull
    private final ShowSchedulerConfiguration configuration;

    @Override
    public void handle(ScheduleShowCommand command) {
        var movie = movieCatalog.get(command.getMovieId());
        var show = new Show(command, movie, configuration);
        if(suspensionChecker.anySuspensionsAtTimeOf(show)) {
            throw new CinemaHallNotAvailableException();
        }
        operationLocker.lock(command.getCinemaId().toString(), command.getCinemaHallId().toString(), () -> {
            if(showRepository.anyShowsCollidingWith(show)) {
                throw new CinemaHallNotAvailableException();
            }
            showRepository.save(show);
            return null;
        });
    }
}
