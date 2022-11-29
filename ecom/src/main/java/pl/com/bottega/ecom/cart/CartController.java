package pl.com.bottega.ecom.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.ecom.user.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Scope("request")
class CartController {

    private final CartService cartService;

    @GetMapping
    CartDto getCart(Authentication authentication) {
        return cartService.getCart(getUserId(authentication));
    }

    @PostMapping
    void addToCart(@Valid @RequestBody AddToCartRequest request, Authentication authentication) {
        cartService.addToCart(request.productId, getUserId(authentication));
    }

    @DeleteMapping
    void removeFromCart(@Valid @RequestBody RemoveFromCartRequest request, Authentication authentication) {
        cartService.removeFromCart(request.productId, getUserId(authentication));
    }

    @PutMapping
    void changeQuantity(@Valid @RequestBody ChangeQuantityInCartRequest request, Authentication authentication) {
        cartService.changeQuantity(request.productId, getUserId(authentication), request.newQuantity);
    }

    private UUID getUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
    
    @Data
    static class AddToCartRequest {
        @NotNull
        UUID productId;
    }

    @Data
    static class RemoveFromCartRequest {
        @NotNull
        UUID productId;
    }

    @Data
    static class ChangeQuantityInCartRequest {
        @NotNull
        UUID productId;
        @Min(1)
        Long newQuantity;
    }
}
