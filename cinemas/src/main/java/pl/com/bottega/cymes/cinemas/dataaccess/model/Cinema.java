package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.IDENTITY;

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
