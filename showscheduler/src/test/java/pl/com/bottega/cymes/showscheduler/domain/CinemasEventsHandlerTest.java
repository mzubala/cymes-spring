package pl.com.bottega.cymes.showscheduler.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.cymes.cinemas.events.CinemaHallSuspendedEvent;
import pl.com.bottega.cymes.cinemas.events.CinemaSuspendedEvent;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CinemasEventsHandlerTest {

    private ShowRepository showRepository = mock(ShowRepository.class);

    private CinemasEventsHandler handler = new CinemasEventsHandler(showRepository);

    @Test
    public void cancelsShowsWhenCinemaIsSuspended() {
        // given
        var s1 = new ShowExample().toShow();
        var s2 = new ShowExample().toShow();
        var event = new CinemaSuspendedEvent(s1.getCinemaId(), s1.getStart(), s2.getEnd());
        when(showRepository.findShowsBetweenInCinema(event.getFrom(), event.getUntil(), event.getCinemaId()))
            .thenReturn(Stream.of(s1, s2));

        // when
        handler.handle(event);

        // then
        assertThat(s1.isCanceled()).isTrue();
        assertThat(s2.isCanceled()).isTrue();
        verify(showRepository).save(s1);
        verify(showRepository).save(s2);
    }

    @Test
    public void cancelsShowsWhenCinemaHallIsSuspended() {
        // given
        var s1 = new ShowExample().toShow();
        var s2 = new ShowExample().toShow();
        var event = new CinemaHallSuspendedEvent(s1.getCinemaHallId(), s1.getStart(), s2.getEnd());
        when(showRepository.findShowsBetweenInCinemaHall(event.getFrom(), event.getUntil(), event.getCinemaHallId()))
            .thenReturn(Stream.of(s1, s2));

        // when
        handler.handle(event);

        // then
        assertThat(s1.isCanceled()).isTrue();
        assertThat(s2.isCanceled()).isTrue();
        verify(showRepository).save(s1);
        verify(showRepository).save(s2);
    }
}
