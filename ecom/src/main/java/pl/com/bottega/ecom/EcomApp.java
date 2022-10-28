package pl.com.bottega.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.com.bottega.ecom.cart.PLTaxPolicyProperties;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(PLTaxPolicyProperties.class)
class EcomApp {
    public static void main(String[] args) {
        SpringApplication.run(EcomApp.class, args);
    }
}
