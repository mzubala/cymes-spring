package pl.com.bottega.ecom.cart;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
class CartDto {
    UUID userId;
    UUID cartId;
    BigDecimal subtotal;
    BigDecimal total;
}
