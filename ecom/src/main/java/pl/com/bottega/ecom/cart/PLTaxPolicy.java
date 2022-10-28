package pl.com.bottega.ecom.cart;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(value = "tax-policy", havingValue = "pl")
@RequiredArgsConstructor
@Log
class PLTaxPolicy implements TaxPolicy {

    private final PLTaxPolicyProperties properties;

    @Override
    public BigDecimal calculateTax(TaxQuery query) {
        log.info("Properties = " + properties);
        return query.getProduct().getPrice().multiply(properties.jedzenie);
    }
}
