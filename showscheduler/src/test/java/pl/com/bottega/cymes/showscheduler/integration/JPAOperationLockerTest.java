package pl.com.bottega.cymes.showscheduler.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.JPAOperationLocker;
import pl.com.bottega.cymes.showscheduler.adapters.JPAShowRepository;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JPAOperationLockerTest {

    @Inject
    private JPAOperationLocker locker;

    @Inject
    private JPAShowRepository jpaShowRepository;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void createsShowWithinLocker() {
        // given
        var show = new ShowExample().toShow();

        // when
        locker.lock(show.getCinemaId().toString(), show.getCinemaHallId().toString(), () -> {
            jpaShowRepository.save(show);
            return null;
        });

        // then
        assertThat(jpaShowRepository.get(show.getId())).isNotNull();
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
            jpaShowRepository.save(show);
            return null;
        }));

        // then
        shows.forEach((show) -> assertThat(jpaShowRepository.get(show.getId())).isNotNull());
    }

    @Test
    public void locksOperationsInMultithreadedEnvironment() {
        // given
        var threadsCount = 10;
        var executorService = Executors.newFixedThreadPool(threadsCount);
        Instant start = Instant.now().plusMillis(10);
        Instant end = Instant.now().plusMillis(1000000);
        Runnable operation = () -> {
            var show = new ShowExample().withCinemaHallId(10L).withCinemaId(10L).withStart(start).withEnd(end).toShow();
            locker.lock(show.getCinemaId().toString(), show.getCinemaHallId().toString(), () -> {
                if (!jpaShowRepository.anyShowsCollidingWith(show)) {
                    jpaShowRepository.save(show);
                };
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
        List results = entityManager.createQuery("SELECT e FROM Show e").getResultList();
        assertThat(results).hasSize(1);
    }
}
