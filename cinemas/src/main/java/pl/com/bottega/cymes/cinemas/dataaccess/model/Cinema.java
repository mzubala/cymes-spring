package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NamedQueries({
    @NamedQuery(
        name = "Cinema.getBasicCinemaInfo",
        query = "SELECT new pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto(c.id, c.name, c.city) FROM Cinema c"
    ),
    @NamedQuery(
        name = "Cinema.getDetailedCinemaInfo",
        query = "SELECT new pl.com.bottega.cymes.cinemas.services.dto.DetailedCinemaInfoDto(c.id, c.name, c.city) " +
            "FROM Cinema c WHERE c.id = :cinemaId"
    )
})
@Table(
    indexes = {
        @Index(columnList = "city, name", unique = true)
    }
)
public class Cinema {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String city;
}
