package pl.com.bottega.ecom.cart;

import lombok.NoArgsConstructor;
import pl.com.bottega.ecom.catalog.Product;
import pl.com.bottega.ecom.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
class Cart {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "index")
    private List<CartItem> items = new LinkedList<>();

    @ManyToOne
    private User user;

    private Boolean active;

    Cart(User user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.active = true;
    }

    void add(Product product) {
        var existingItem = items.stream().filter(item -> item.contains(product)).findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().increaseCount();
        } else {
            items.add(new CartItem(this, product));
        }
    }

    void remove(UUID productId) {
        for (var i = items.iterator(); i.hasNext(); ) {
            if (i.next().contains(productId)) {
                i.remove();
                return;
            }
        }
        throw entityNotFoundException(productId);
    }

    void changeQuantity(UUID productId, Long newQuantity) {
        var cartItem = items.stream().filter(item -> item.contains(productId)).findFirst()
            .orElseThrow(() -> entityNotFoundException(productId));
        cartItem.changeQuantity(newQuantity);
    }

    private static EntityNotFoundException entityNotFoundException(UUID productId) {
        return new EntityNotFoundException(String.format("Product with id %s is not found", productId));
    }

    UUID getId() {
        return id;
    }
}
