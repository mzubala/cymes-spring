package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Log
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        log.info("Creating controller");
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<CreateProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        log.info(String.format("Create product %s", request));
        var id = productService.create(new CreateProductCommand(request.name, request.categoryId));
        return new ResponseEntity<>(new CreateProductResponse(id), HttpStatus.CREATED);
    }

    @GetMapping
    List<SearchedProductResponse> searchProducts(SearchProductsRequest request) {
        log.info(String.format("Search products %s", request));
        return List.of(
            new SearchedProductResponse(UUID.randomUUID(), UUID.randomUUID(), "Chleb"),
            new SearchedProductResponse(UUID.randomUUID(), UUID.randomUUID(), "Bułka")
        );
    }

    @PutMapping("/{productId}")
    void updateProduct(@PathVariable UUID productId, @RequestBody UpdateProductRequest request) {
        log.info(String.format("Updating product %s, %s", productId, request));
    }
}

@Data
class CreateProductRequest {
    String name;
    UUID categoryId;
}

@Data
@AllArgsConstructor
class CreateProductResponse {
    UUID productId;
}

@Data
@AllArgsConstructor
class SearchedProductResponse {
    UUID productId;
    UUID categoryId;
    String name;
}

@Data
class UpdateProductRequest {
    String name;
    UUID categoryId;
}

@Data
class SearchProductsRequest {
    String phrase;
    UUID categoryId;
}