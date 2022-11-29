package pl.com.bottega.ecom.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Scope("request")
class CartController {

    private static final UUID TMP_USER_ID = UUID.fromString("386e8098-5605-11ed-bdc3-0242ac120002");

    private final CartService cartService;

    @GetMapping
    CartDto getCart() {
        return cartService.getCart(TMP_USER_ID);
    }

    @PostMapping
    void addToCart(@Valid @RequestBody AddToCartRequest request) {
        cartService.addToCart(request.productId, TMP_USER_ID);
    }

    @DeleteMapping
    void removeFromCart(@Valid @RequestBody RemoveFromCartRequest request) {
        cartService.removeFromCart(request.productId, TMP_USER_ID);
    }

    @PutMapping
    void changeQuantity(@Valid @RequestBody ChangeQuantityInCartRequest request) {
        cartService.changeQuantity(request.productId, TMP_USER_ID, request.newQuantity);
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
