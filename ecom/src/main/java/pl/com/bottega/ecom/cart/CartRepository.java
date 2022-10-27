package pl.com.bottega.ecom.cart;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface CartRepository extends Repository<Cart, UUID> {
    Optional<Cart> findByActiveAndUserId(boolean active, UUID userId);

    Cart getByActiveAndUserId(boolean active, UUID userId);

    void save(Cart cart);

}
