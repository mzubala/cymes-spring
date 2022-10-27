package pl.com.bottega.ecom.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.ecom.catalog.CatalogFacade;
import pl.com.bottega.ecom.user.UserFacade;

import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
class CartService {

    private final CartRepository cartRepository;
    private final UserFacade userFacade;
    private final CatalogFacade catalogFacade;

    public void addToCart(UUID productId, UUID userId) {
        var cart = findOrCreateCart(userId);
        cart.add(catalogFacade.getProductById(productId));
        cartRepository.save(cart);
    }

    private Cart findOrCreateCart(UUID userId) {
        return cartRepository.findByActiveAndUserId(true, userId).orElseGet(() ->
            new Cart(userFacade.getById(userId))
        );
    }

}
