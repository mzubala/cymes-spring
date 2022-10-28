package pl.com.bottega.ecom.cart;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "pl-tax-policy")
@Data
public class PLTaxPolicyProperties {
    BigDecimal jedzenie;
    BigDecimal budowa;
}
