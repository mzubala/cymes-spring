package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
class Category {
    @Id
    private UUID id;
    private String name;

    void setName(String name) {
        this.name = name;
    }
}
