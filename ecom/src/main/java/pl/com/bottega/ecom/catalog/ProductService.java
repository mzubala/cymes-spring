package pl.com.bottega.ecom.catalog;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.ecom.UserCommand;
import pl.com.bottega.ecom.infrastructure.Audit;
import pl.com.bottega.ecom.infrastructure.Timed;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.com.bottega.ecom.catalog.ProductSpecifications.categoryIdEquals;
import static pl.com.bottega.ecom.catalog.ProductSpecifications.nameMatches;

@Component
@RequiredArgsConstructor
@Transactional
@Audit
class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ApplicationEventPublisher publisher;

    @Timed
    public UUID create(CreateProductCommand createProductCommand) {
        var category = categoryRepository.getById(createProductCommand.getCategoryId());
        var product = new Product(
            UUID.randomUUID(),
            createProductCommand.getName(),
            createProductCommand.getPrice(),
            category
        );
        productRepository.save(product);
        return product.getId();
    }

    public void update(UpdateProductCommand updateProductCommand) {
        var category = categoryRepository.getById(updateProductCommand.getCategoryId());
        var product = productRepository.getById(updateProductCommand.getProductId());
        product.setName(updateProductCommand.getName());
        product.setCategory(category);
        if(!product.getPrice().equals(updateProductCommand.price)) {
            publisher.publishEvent(new ProductPriceChangedEvent(
                product.getId(),
                product.getPrice(),
                updateProductCommand.price
            ));
        }
        product.setPrice(updateProductCommand.getPrice());
        productRepository.save(product);
    }

    public List<ProductDto> search(String phrase, UUID categoryId) {
        return productRepository.findAll(nameMatches(phrase).and(categoryIdEquals(categoryId))
        ).stream().map(p -> new ProductDto(p.getId(), p.getCategory().getId(), p.getName())).collect(Collectors.toList());
    }

    @Value
    static class CreateProductCommand extends UserCommand {
        String name;
        UUID categoryId;
        BigDecimal price;
    }

    @Value
    static class UpdateProductCommand extends UserCommand {
        UUID productId;
        String name;
        BigDecimal price;
        UUID categoryId;
    }
}
