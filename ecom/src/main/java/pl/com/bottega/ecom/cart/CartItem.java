package pl.com.bottega.ecom.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.com.bottega.ecom.catalog.Product;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
        this.id = new CartItemId(cart.getId(), product.getId());
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
