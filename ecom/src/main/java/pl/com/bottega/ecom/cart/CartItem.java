package pl.com.bottega.ecom.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.com.bottega.ecom.catalog.Product;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
class CartItem {
    @EmbeddedId
    private CartItemId id;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("cartId")
    private Cart cart;

    private Long itemsCount;

    CartItem(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
        this.itemsCount = 1L;
    }

    boolean contains(Product product) {
        return product.getId().equals(this.product.getId());
    }

    void increaseCount() {
        itemsCount++;
    }
}

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
class CartItemId implements Serializable {
    private UUID cartId;
    private UUID productId;
}
