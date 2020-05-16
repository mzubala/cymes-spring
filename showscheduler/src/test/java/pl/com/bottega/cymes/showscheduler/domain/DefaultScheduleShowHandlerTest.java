package pl.com.bottega.cymes.showscheduler.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
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
        verify(showRepository).save(argThat((Show show) -> show.getId().equals(cmd.getShowId())));
    }

    @Test
    public void checksCinemaSuspension() {
        // when
        handler.handle(cmd);

        // then
        verify(suspensionChecker, only()).isSuspended(cmd.getCinemaId(), cmd.getCinemaHallId());
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
        when(suspensionChecker.isSuspended(cmd.getCinemaId(), cmd.getCinemaHallId())).thenReturn(true);

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
}
