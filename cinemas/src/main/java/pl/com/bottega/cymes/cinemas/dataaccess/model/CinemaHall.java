package pl.com.bottega.cymes.cinemas.dataaccess.model;

import jdk.jfr.Name;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

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
