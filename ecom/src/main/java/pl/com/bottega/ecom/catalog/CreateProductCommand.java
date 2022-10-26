package pl.com.bottega.ecom.catalog;

import lombok.Value;

import java.util.UUID;

@Value
class CreateProductCommand {
    String name;
    UUID categoryId;
}
