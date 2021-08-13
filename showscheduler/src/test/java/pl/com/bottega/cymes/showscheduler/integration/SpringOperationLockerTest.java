package pl.com.bottega.cymes.showscheduler.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.showscheduler.adapters.db.SpringOperationLocker;
import pl.com.bottega.cymes.showscheduler.adapters.db.SpringShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class SpringOperationLockerTest {

    @Autowired
    private SpringOperationLocker locker;

    @Autowired
    private SpringShowRepository springShowRepository;

    @Test
    public void createsShowWithinLocker() {
        // given
        var show = new ShowExample().toShow();

        // when
        locker.lock(show.getCinemaId().toString(), show.getCinemaHallId().toString(), () -> {
            springShowRepository.save(show);
            return null;
        });

        // then
        assertThat(springShowRepository.get(show.getId())).isNotNull();
    }

    @Test
    public void createsCoupleShowsWithinLocker() {
        // given
        var shows = List.of(
            new ShowExample().withCinemaId(1L).withCinemaHallId(1L).toShow(),
            new ShowExample().withCinemaId(1L).withCinemaHallId(1L).toShow(),
            new ShowExample().withCinemaId(1L).withCinemaHallId(2L).toShow(),
            new ShowExample().withCinemaId(2L).withCinemaHallId(2L).toShow()
        );

        // when
        shows.forEach((show) -> locker.lock(show.getCinemaId().toString(), show.getCinemaHallId().toString(), () -> {
            springShowRepository.save(show);
            return null;
        }));

        // then
        shows.forEach((show) -> assertThat(springShowRepository.get(show.getId())).isNotNull());
    }

    @Test
    public void locksOperationsInMultithreadedEnvironment() {
        // given
        var threadsCount = 10;
        var executorService = Executors.newFixedThreadPool(threadsCount);
        var showExample = new ShowExample()
                .withStart(Instant.now().plusSeconds(120))
                .withEnd(Instant.now().plusSeconds(1000000));
        Runnable operation = () -> {
            var show = showExample.withId(UUID.randomUUID()).toShow();
            locker.lock(show.getCinemaId().toString(), show.getCinemaHallId().toString(), () -> {
                if (!springShowRepository.anyShowsCollidingWith(show)) {
                    springShowRepository.save(show);
                }
                return null;
            });
        };

        // when
        List<Future<?>> futures = IntStream.range(1, threadsCount * 2).mapToObj((i) -> executorService.submit(operation)).collect(toList());
        futures.forEach(f -> {
            try {
                f.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        executorService.shutdown();

        // then
        assertThat(springShowRepository.countShows()).isEqualTo(1L);
    }
}
