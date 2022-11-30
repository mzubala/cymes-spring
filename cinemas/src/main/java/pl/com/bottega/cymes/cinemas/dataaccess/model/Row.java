package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Row {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer number;

    @OneToMany(cascade = ALL)
    @JoinColumn
    @OrderColumn
    private List<RowElement> elements;
}
