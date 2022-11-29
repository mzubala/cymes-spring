package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @PostMapping
    ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        var id = productService.create(new ProductService.CreateProductCommand(request.name, request.categoryId, request.price));
        return new ResponseEntity<>(new CreateProductResponse(id), HttpStatus.CREATED);
    }

    @GetMapping
    List<ProductDto> searchProducts(SearchProductsRequest request) {
        return productService.search(request.phrase, request.categoryId);
    }

    @PutMapping("/{productId}")
    void updateProduct(@PathVariable UUID productId, @Valid @RequestBody UpdateProductRequest request) {
        productService.update(new ProductService.UpdateProductCommand(productId, request.name, request.price, request.categoryId));
    }
}

@Data
class CreateProductRequest {
    @NotBlank
    String name;
    @NotNull
    UUID categoryId;
    @NotNull
    @Min(value = 0)
    BigDecimal price;
}

@Data
@AllArgsConstructor
class CreateProductResponse {
    UUID productId;
}

@Data
class UpdateProductRequest {
    @NotBlank
    String name;
    @NotNull
    @Min(value = 0)
    BigDecimal price;
    @NotNull
    UUID categoryId;
}

@Data
class SearchProductsRequest {
    String phrase;
    UUID categoryId;
}