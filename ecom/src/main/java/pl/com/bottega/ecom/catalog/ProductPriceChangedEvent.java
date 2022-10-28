package pl.com.bottega.ecom.catalog;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class ProductPriceChangedEvent {
    UUID productId;
    BigDecimal oldPrice;
    BigDecimal newPrice;
}
