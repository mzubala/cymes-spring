package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
class CategoryDto {
    UUID categoryId;
    String name;
}
