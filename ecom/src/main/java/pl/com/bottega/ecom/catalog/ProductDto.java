package pl.com.bottega.ecom.catalog;

import lombok.Value;

import java.util.UUID;

@Value
class ProductDto {
    UUID productId;
    UUID categoryId;
    String name;
}
