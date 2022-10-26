package pl.com.bottega.ecom.catalog;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.com.bottega.ecom.catalog.ProductSpecifications.categoryIdEquals;
import static pl.com.bottega.ecom.catalog.ProductSpecifications.nameMatches;

@Component
@RequiredArgsConstructor
class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    UUID create(CreateProductCommand createProductCommand) {
        var category = categoryRepository.getById(createProductCommand.getCategoryId());
        var product = new Product(
            UUID.randomUUID(),
            createProductCommand.getName(),
            category
        );
        productRepository.save(product);
        return product.getId();
    }

    void update(UpdateProductCommand updateProductCommand) {
        var category = categoryRepository.getById(updateProductCommand.getCategoryId());
        var product = productRepository.getById(updateProductCommand.getProductId());
        product.setName(updateProductCommand.getName());
        product.setCategory(category);
        productRepository.save(product);
    }

    List<ProductDto> search(String phrase, UUID categoryId) {
        return productRepository.findAll(nameMatches(phrase).and(categoryIdEquals(categoryId))
        ).stream().map(p -> new ProductDto(p.getId(), p.getCategory().getId(), p.getName())).collect(Collectors.toList());
    }

    @Value
    static class CreateProductCommand {
        String name;
        UUID categoryId;
    }

    @Value
    static class UpdateProductCommand {
        UUID productId;
        String name;
        UUID categoryId;
    }
}
