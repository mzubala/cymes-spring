package pl.com.bottega.ecom.catalog;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class ProductService {
    UUID create(CreateProductCommand createProductCommand) {
        return UUID.randomUUID();
    }

    void update(UpdateProductCommand updateProductCommand) {

    }

    List<ProductDto> search(String phrase, UUID categoryId) {
        return List.of(
            new ProductDto(UUID.randomUUID(), UUID.randomUUID(), "Chleb"),
            new ProductDto(UUID.randomUUID(), UUID.randomUUID(), "Bułka")
        );
    }
}
