package pl.com.bottega.cymes.cinemas.dataaccess.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class GenericDao<EntityType, PrimaryKeyType> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<EntityType> entityTypeClass;

    public GenericDao() {
        entityTypeClass = (Class<EntityType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void save(EntityType entity) {
        entityManager.persist(entity);
    }

    public Optional<EntityType> findOptionally(PrimaryKeyType id) {
        return Optional.ofNullable(entityManager.find(entityTypeClass, id));
    }

    public EntityType find(PrimaryKeyType id) {
         return findOptionally(id).orElseThrow(() -> new EntityNotFoundException(entityTypeClass, id));
    }

    public EntityType getReference(PrimaryKeyType id) {
        return entityManager.getReference(entityTypeClass, id);
    }

    public static class EntityNotFoundException extends RuntimeException {
        EntityNotFoundException(Class klass, Object id) {
            super(String.format("%s with id=%s cannot be found", klass.getName(), id.toString()));
        }
    }
}

