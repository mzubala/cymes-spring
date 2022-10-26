package pl.com.bottega.ecom.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

interface ProductRepository extends Repository<Product, UUID> {
    void save(Product product);

    Product getById(UUID product);

    List<Product> findAll(Specification<Product> specification);
}

class ProductSpecifications {
    static Specification<Product> nameMatches(String phrase) {
        return (root, query, criteriaBuilder) -> {
            if (phrase != null) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + phrase.toLowerCase() + "%");
            }
            return null;
        };
    }

    static Specification<Product> categoryIdEquals(UUID categoryId) {
        return (root, query, criteriaBuilder) -> {
            if(categoryId != null) {
                return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
            }
            return null;
        };
    }
}