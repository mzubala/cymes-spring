package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

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
