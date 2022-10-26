package pl.com.bottega.ecom.catalog;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

interface CategoryRepository extends Repository<Category, UUID> {

    void save(Category category);

    List<Category> findByNameLike(String pattern);

    Category getById(UUID categoryId);
}
