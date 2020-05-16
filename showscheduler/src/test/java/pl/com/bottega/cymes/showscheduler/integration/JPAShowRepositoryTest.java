package pl.com.bottega.cymes.showscheduler.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.JPAShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import javax.inject.Inject;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(Arquillian.class)
public class JPAShowRepositoryTest {
    @Inject
    private JPAShowRepository jpaShowRepository;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Test
    public void savesAndReadsShow() {
        // given
        var show = new ShowExample()
            .withStart(Instant.parse("2010-01-01T20:00:00.00Z"))
            .withEnd(Instant.parse("2010-01-01T22:30:00.00Z"))
            .toShow();

        // when
        jpaShowRepository.save(show);
        var showFetched = jpaShowRepository.get(show.getId());

        // then
        assertThat(show.getId()).isEqualTo(showFetched.getId());
        assertThat(show.getMovieId()).isEqualTo(showFetched.getMovieId());
        assertThat(show.getStart()).isEqualTo(showFetched.getStart());
        assertThat(show.getEnd()).isEqualTo(showFetched.getEnd());
    }

    @Test
    public void checksForCollidingShows() {
        // given
        jpaShowRepository.save(show("16:30", "18:45"));

        // then
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("14:00", "16:00"))).isFalse();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("14:00", "16:31"))).isTrue();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("15:00", "17:30"))).isTrue();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("16:40", "18:40"))).isTrue();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("18:00", "20:40"))).isTrue();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("18:45", "20:40"))).isTrue();
        assertThat(jpaShowRepository.anyShowsCollidingWith(show("18:46", "20:40"))).isFalse();
    }

    private Show show(String startHour, String endHour) {
        var start = String.format("2010-11-30T%s:00.00Z", startHour);
        var end = String.format("2010-11-30T%s:00.00Z", endHour);
        System.out.println(start);
        return new ShowExample()
            .withStart(Instant.parse(start))
            .withEnd(Instant.parse(end))
            .toShow();
    }
}
