package pl.com.bottega.ecom.catalog;

import lombok.Value;

import java.util.UUID;

@Value
class UpdateProductCommand {
    UUID productId;
    String name;
    UUID categoryId;
}
