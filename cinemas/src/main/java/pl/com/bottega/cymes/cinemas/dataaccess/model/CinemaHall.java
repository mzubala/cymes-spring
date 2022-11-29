package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(
    indexes = {
        @Index(unique = true, columnList = "name, cinema_id")
    }
)
@NamedQueries({
    @NamedQuery(
        name = "CinemaHall.getBasicInfo",
        query = "SELECT NEW pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaHallInfoDto(h.id, h.name, h.capacity) FROM CinemaHall h WHERE h.cinema.id = :cinemaId"
    )
})
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Integer capacity;

    @OneToMany(cascade = ALL)
    @JoinColumn
    private List<Row> rows;

    @ManyToOne
    private Cinema cinema;
}
