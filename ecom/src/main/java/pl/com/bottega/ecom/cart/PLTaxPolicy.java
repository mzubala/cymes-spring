package pl.com.bottega.ecom.cart;

import java.math.BigDecimal;

class PLTaxPolicy implements TaxPolicy {
    @Override
    public BigDecimal calculateTax(TaxQuery query) {
        return BigDecimal.ZERO;
    }
}
