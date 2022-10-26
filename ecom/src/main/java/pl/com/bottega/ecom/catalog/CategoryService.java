package pl.com.bottega.ecom.catalog;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class CategoryService {

    UUID create(CreateCategoryCommand createCategoryCommand) {
        return UUID.randomUUID();
    }

    List<CategoryDto> search(String searchPhrase) {
        return List.of(
            new CategoryDto(UUID.randomUUID(), "Jedzenie"),
            new CategoryDto(UUID.randomUUID(), "Moda"),
            new CategoryDto(UUID.randomUUID(), "Uroda")
        );
    }

    void update(UpdateCategoryCommand updateCategoryCommand) {

    }
}
