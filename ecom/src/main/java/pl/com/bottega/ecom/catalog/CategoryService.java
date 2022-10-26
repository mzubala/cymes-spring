package pl.com.bottega.ecom.catalog;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log
class CategoryService {

    CategoryService() {
        log.info("Creating service");
    }

    UUID create(CreateCategoryCommand createCategoryCommand) {
        log.info("Creating category");
        return UUID.randomUUID();
    }
}
