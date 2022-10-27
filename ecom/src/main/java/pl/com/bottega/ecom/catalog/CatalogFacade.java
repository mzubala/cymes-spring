package pl.com.bottega.ecom.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CatalogFacade {

    private final ProductRepository productRepository;

    public Product getProductById(UUID productId) {
        return productRepository.getById(productId);
    }
}
