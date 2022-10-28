package pl.com.bottega.ecom.cart;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

//@Component
//@ConditionalOnProperty(value = "tax-policy", havingValue = "us")
class USTaxPolicy implements TaxPolicy {
    @Override
    public BigDecimal calculateTax(TaxQuery query) {
        return BigDecimal.TEN;
    }
}
