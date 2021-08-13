package pl.com.bottega.cymes.showscheduler.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.cymes.showscheduler.domain.ports.MovieCatalog;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowSchedulerConfiguration;
import pl.com.bottega.cymes.showscheduler.domain.ports.SuspensionChecker;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultScheduleShowHandlerTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private MovieCatalog movieCatalog;

    private FakeOperationLocker operationLocker = new FakeOperationLocker();

    @Mock
    private SuspensionChecker suspensionChecker;

    @Mock
    private ShowSchedulerConfiguration configuration;

    private DefaultScheduleShowHandler handler;

    private ScheduleShowCommand cmd;

    @BeforeEach
    public void setup() {
        // given
        handler = new DefaultScheduleShowHandler(movieCatalog, showRepository, suspensionChecker, operationLocker, configuration);
        cmd = new ScheduleShowCommandExample().toCommand();
        var movie = new MovieExample().withId(cmd.getMovieId()).toMovie();
        when(movieCatalog.get(cmd.getMovieId())).thenReturn(movie);
        when(configuration.showReservationBuffer()).thenReturn(Duration.ofMinutes(10));
    }

    @Test
    public void savesScheduledShowInRepository() {
        // when
        handler.handle(cmd);

        // then
        verify(showRepository).save(showArg());
    }

    @Test
    public void checksCinemaSuspension() {
        // when
        handler.handle(cmd);

        // then
        verify(suspensionChecker, only()).anySuspensionsAtTimeOf(showArg());
    }

    @Test
    public void checksCollisionsWithOtherShows() {
        // when
        handler.handle(cmd);

        // then
        verify(showRepository).anyShowsCollidingWith(argThat((show) -> show.getId().equals(cmd.getShowId())));
    }

    @Test
    public void throwsExceptionWhenCinemaOrCinemaHallAreSuspended() {
        // given
        when(suspensionChecker.anySuspensionsAtTimeOf(showArg())).thenReturn(true);

        // then
        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(CinemaHallNotAvailableException.class);
        operationLocker.assertThatNoErrorOccuredDuringLockedOperation();
    }

    @Test
    public void throwsExceptionWhenOtherShowsAreColliding() {
        // given
        when(showRepository.anyShowsCollidingWith(any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(CinemaHallNotAvailableException.class);
        operationLocker.assertThatErrorOccuredDuringLockedOperation();
    }

    private Show showArg() {
        return argThat((Show show) -> show.getId().equals(cmd.getShowId()));
    }
}
