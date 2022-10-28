package pl.com.bottega.ecom.cart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.ecom.catalog.CatalogFacade;
import pl.com.bottega.ecom.user.UserFacade;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Component
@Transactional
class CartService {

    private final CartRepository cartRepository;
    private final UserFacade userFacade;
    private final CatalogFacade catalogFacade;

    private final TaxPolicy taxPolicy;

    CartService(CartRepository cartRepository, UserFacade userFacade, CatalogFacade catalogFacade, TaxPolicy taxPolicy) {
        this.cartRepository = cartRepository;
        this.userFacade = userFacade;
        this.catalogFacade = catalogFacade;
        this.taxPolicy = taxPolicy;
    }

    public void addToCart(UUID productId, UUID userId) {
        var cart = findOrCreateCart(userId);
        cart.add(catalogFacade.getProductById(productId));
        cartRepository.save(cart);
    }

    public void removeFromCart(UUID productId, UUID userId) {
        var cart = getCartOrThrow(userId);
        cart.remove(productId);
        cartRepository.save(cart);
    }

    public void changeQuantity(UUID productId, UUID userId, Long newQuantity) {
        var cart = getCartOrThrow(userId);
        cart.changeQuantity(productId, newQuantity);
        cartRepository.save(cart);
    }

    public CartDto getCart(UUID userId) {
        var cart = findOrCreateCart(userId);
        return new CartDto(
            userId,
            cart.getId(),
            cart.getSubtotal(),
            cart.getTotal(taxPolicy)
        );
    }

    private Cart getCartOrThrow(UUID userId) {
        return cartRepository.findByActiveAndUserId(true, userId).orElseThrow(() -> new EntityNotFoundException(String.format("Active cart for user %s not found", userId)));
    }

    private Cart findOrCreateCart(UUID userId) {
        return cartRepository.findByActiveAndUserId(true, userId).orElseGet(() ->
            new Cart(userFacade.getById(userId))
        );
    }

}
