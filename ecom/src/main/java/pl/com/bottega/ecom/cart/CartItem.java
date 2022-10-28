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
import java.math.BigDecimal;
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

    boolean contains(UUID productId) {
        return product.getId().equals(productId);
    }

    void changeQuantity(Long newQuantity) {
        if(newQuantity < 1) {
            throw new IllegalArgumentException();
        }
        this.itemsCount = newQuantity;
    }

    BigDecimal getSubtotal() {
        return product.getPrice().multiply(new BigDecimal(itemsCount));
    }

    BigDecimal getTotal(TaxPolicy taxPolicy) {
        return getSubtotal().add(taxPolicy.calculateTax(new TaxQuery(
            product,
            cart.getUser()
        )).multiply(new BigDecimal(itemsCount)));
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
