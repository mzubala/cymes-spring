package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public
class Product {

    @Id
    private UUID id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    private Category category;

    void setName(String name) {
        this.name = name;
    }

    void setCategory(Category category) {
        this.category = category;
    }

    void setPrice(BigDecimal price) {
        this.price = price;
    }
}