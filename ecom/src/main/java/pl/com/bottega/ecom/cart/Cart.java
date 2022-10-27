package pl.com.bottega.ecom.cart;

import lombok.NoArgsConstructor;
import pl.com.bottega.ecom.user.User;

import javax.persistence.Entity;
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
    @OneToMany(mappedBy = "cart")
    @OrderColumn(name = "index")
    private List<CartItem> items = new LinkedList<>();

    @ManyToOne
    private User user;

    private Boolean active;
}
