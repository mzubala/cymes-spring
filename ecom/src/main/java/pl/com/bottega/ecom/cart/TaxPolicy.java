package pl.com.bottega.ecom.cart;

import lombok.Value;
import pl.com.bottega.ecom.catalog.Product;
import pl.com.bottega.ecom.user.User;

import java.math.BigDecimal;

interface TaxPolicy {
    BigDecimal calculateTax(TaxQuery query);
}

@Value
class TaxQuery {
    Product product;
    User user;
}
