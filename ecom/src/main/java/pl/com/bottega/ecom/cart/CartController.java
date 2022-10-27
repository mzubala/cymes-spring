package pl.com.bottega.ecom.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
class CartController {

    private final CartService cartService;

    @GetMapping
    CartDto getCart() {
        return null;
    }

    @PostMapping
    void addToCart(@RequestBody AddToCartRequest addToCartRequest) {

    }

    @DeleteMapping
    void removeFromCart(@RequestBody RemoveFromCartRequest addToCartRequest) {

    }

    @PutMapping
    void changeQuantity(@RequestBody ChangeQuantityInCartRequest addToCartRequest) {

    }

    @Data
    static class AddToCartRequest {
        UUID productId;
    }

    @Data
    static class RemoveFromCartRequest {
        UUID productId;
    }

    @Data
    static class ChangeQuantityInCartRequest {
        UUID productId;
        Long newQuantity;
    }
}
