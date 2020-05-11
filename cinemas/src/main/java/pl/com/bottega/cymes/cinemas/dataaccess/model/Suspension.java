package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@NamedQueries({
    @NamedQuery(
        name = "Suspension.getActiveCinemaSuspensions",
        query = "SELECT new pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto(s.id, s.from, s.until) FROM Suspension s WHERE s.cinema.id = :cinemaId AND s.active = true"
    ),
    @NamedQuery(
        name = "Suspension.getActiveCinemaSuspensionAt",
        query = "SELECT s FROM Suspension s WHERE s.cinema.id = :cinemaId AND s.active = true AND :at >= s.from AND :at <= s.until"
    ),
    @NamedQuery(
        name = "Suspension.getActiveCinemaHallSuspensions",
        query = "SELECT new pl.com.bottega.cymes.cinemas.services.dto.SuspensionDto(s.id, s.from, s.until) FROM Suspension s WHERE s.cinemaHall.id = :cinemaHallId AND s.active = true"
    ),
    @NamedQuery(
        name = "Suspension.getActiveCinemaHallSuspensionAt",
        query = "SELECT s FROM Suspension s WHERE s.cinemaHall.id = :cinemaHallId AND s.active = true AND :at >= s.from AND :at <= s.until"
    )
})
public class Suspension {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(name = "beginning")
    private Instant from;
    private Instant until;
    private Boolean active;

    @ManyToOne
    private Cinema cinema;

    @ManyToOne
    private CinemaHall cinemaHall;
}
