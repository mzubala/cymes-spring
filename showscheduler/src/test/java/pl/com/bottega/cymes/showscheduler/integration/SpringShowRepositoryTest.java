package pl.com.bottega.cymes.showscheduler.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.showscheduler.adapters.db.SpringShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;
import pl.com.bottega.cymes.showscheduler.domain.ports.ShowRepository.ShowNotFoundException;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@IntegrationTest
public class SpringShowRepositoryTest {

    @Autowired
    private SpringShowRepository springShowRepository;

    @Test
    public void savesAndReadsShow() {
        // given
        var show = new ShowExample()
                .withStart(Instant.parse("2010-01-01T20:00:00.00Z"))
                .withEnd(Instant.parse("2010-01-01T22:30:00.00Z"))
                .toShow();

        // when
        springShowRepository.save(show);
        var showFetched = springShowRepository.get(show.getId());

        // then
        assertThat(show.getId()).isEqualTo(showFetched.getId());
        assertThat(show.getMovieId()).isEqualTo(showFetched.getMovieId());
        assertThat(show.getStart()).isEqualTo(showFetched.getStart());
        assertThat(show.getEnd()).isEqualTo(showFetched.getEnd());
    }

    @Test
    public void checksForCollidingShows() {
        // given
        springShowRepository.save(show("16:30", "18:45"));

        // then
        assertThat(springShowRepository.anyShowsCollidingWith(show("14:00", "16:00"))).isFalse();
        assertThat(springShowRepository.anyShowsCollidingWith(show("14:00", "16:31"))).isTrue();
        assertThat(springShowRepository.anyShowsCollidingWith(show("15:00", "17:30"))).isTrue();
        assertThat(springShowRepository.anyShowsCollidingWith(show("16:40", "18:40"))).isTrue();
        assertThat(springShowRepository.anyShowsCollidingWith(show("18:00", "20:40"))).isTrue();
        assertThat(springShowRepository.anyShowsCollidingWith(show("18:45", "20:40"))).isTrue();
        assertThat(springShowRepository.anyShowsCollidingWith(show("18:46", "20:40"))).isFalse();
    }

    @Test
    public void throwsExceptionWhenShowIdIsInvalid() {
        assertThatThrownBy(() -> springShowRepository.get(UUID.randomUUID())).isInstanceOf(ShowNotFoundException.class);
    }

    private Show show(String startHour, String endHour) {
        var start = String.format("2010-11-30T%s:00.00Z", startHour);
        var end = String.format("2010-11-30T%s:00.00Z", endHour);
        return new ShowExample()
                .withStart(Instant.parse(start))
                .withEnd(Instant.parse(end))
                .withCinemaId(1L)
                .withCinemaHallId(1L)
                .toShow();
    }
}
