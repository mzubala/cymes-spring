package pl.com.bottega.ecom.catalog;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log
class ProductService {
    ProductService() {
        log.info("Creating service");
    }

    UUID create(CreateProductCommand createProductCommand) {
        log.info("Creating product");
        return UUID.randomUUID();
    }
}
