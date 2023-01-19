package pl.com.bottega.ecom.cart;

import java.math.BigDecimal;

class USTaxPolicy implements TaxPolicy {
    @Override
    public BigDecimal calculateTax(TaxQuery query) {
        return BigDecimal.TEN;
    }
}
