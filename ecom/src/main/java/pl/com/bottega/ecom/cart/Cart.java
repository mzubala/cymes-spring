package pl.com.bottega.ecom.cart;

import lombok.NoArgsConstructor;
import pl.com.bottega.ecom.catalog.Product;
import pl.com.bottega.ecom.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
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
        if(existingItem.isPresent()) {
            existingItem.get().increaseCount();
        } else {
            items.add(new CartItem(this, product));
        }
    }
}
