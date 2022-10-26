package pl.com.bottega.ecom.catalog;

import lombok.Value;

import java.util.UUID;

@Value
class UpdateCategoryCommand {
    UUID categoryId;
    String name;
}
