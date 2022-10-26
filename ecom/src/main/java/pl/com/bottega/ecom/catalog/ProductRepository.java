package pl.com.bottega.ecom.catalog;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

interface ProductRepository extends Repository<Product, UUID> {
    void save(Product product);

    Product getById(UUID product);

    List<Product> findByNameLikeAndCategoryId(String name, UUID categoryId);

    List<Product> findByNameLike(String name);

    List<Product> findByCategoryId(UUID categoryId);

    List<Product> findAll();
}
