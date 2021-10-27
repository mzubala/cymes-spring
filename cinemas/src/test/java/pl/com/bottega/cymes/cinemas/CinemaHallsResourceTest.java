package pl.com.bottega.cymes.cinemas;


import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaDao;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.CinemaHallDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Cinema;
import pl.com.bottega.cymes.cinemas.dataaccess.model.CinemaHall;
import pl.com.bottega.cymes.cinemas.dataaccess.model.Row;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElement;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElementKind;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class CinemaHallsResourceTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CinemaDao cinemaDao;

    @Autowired
    private CinemaHallDao cinemaHallDao;

    @Autowired
    private CinemasClient cinemasClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void startCountingStatistics() {
        getStatistics().setStatisticsEnabled(true);
    }

    @Test
    public void avoidsNp1SelectWhenGettingCinemaHalls() {
        // given
        var cinema = new Cinema();
        cinema.setCity("Warszwa");
        cinema.setName("Arkadia");
        var cinemaHall = new CinemaHall();
        cinemaHall.setCinema(cinema);
        cinemaHall.setName("S1");
        cinemaHall.setRows(List.of(cinemaHallRow(cinemaHall, 1), cinemaHallRow(cinemaHall, 2)));
        transactionTemplate.executeWithoutResult((transactionCallback) -> {
            cinemaDao.save(cinema);
            cinemaHallDao.save(cinemaHall);
        });

        // when
        getStatistics().clear();
        cinemasClient.getCinemaHall(cinemaHall.getId());

        // then
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }

    private Row cinemaHallRow(CinemaHall cinemaHall, Integer number) {
        var row = new Row();
        row.setNumber(number);
        row.setElements(List.of(rowElement(row, 1), rowElement(row, 2), rowElement(row, 3)));
        return row;
    }

    private RowElement rowElement(Row row, int i) {
        var element = new RowElement();
        element.setElementKind(RowElementKind.SEAT);
        element.setNumber(i);
        element.setIndex(i);
        return element;
    }

    private Statistics getStatistics() {
        return entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    }
}
