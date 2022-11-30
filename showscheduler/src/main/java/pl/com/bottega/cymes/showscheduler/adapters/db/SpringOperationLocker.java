package pl.com.bottega.cymes.showscheduler.adapters.db;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.domain.ports.OperationLocker;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SpringOperationLocker implements OperationLocker {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        var lock = new OperationLock(new OperationLockId(lockKey1, lockKey2));
        entityManager.persist(lock);
        entityManager.flush();
        var result = operation.get();
        entityManager.remove(lock);
        return result;
    }
}

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation_locks")
class OperationLock {
    @EmbeddedId
    private OperationLockId id;
}

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
class OperationLockId implements Serializable {
    private String lockKey1;
    private String lockKey2;
}
