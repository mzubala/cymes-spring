package pl.com.bottega.cymes.cinemas.dataaccess.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class RowElement {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer index;

    private Integer number;

    @Enumerated(STRING)
    private RowElementKind elementKind;
}
