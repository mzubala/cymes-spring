package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.logging.Logger;
import pl.com.bottega.cymes.showscheduler.domain.OperationLocker;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.function.Supplier;

@Dependent
public class JPAOperationLocker implements OperationLocker {

    @Inject
    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(JPAOperationLocker.class);

    @Resource
    private UserTransaction userTransaction;

    @Override
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        var id = new OperationLockId(lockKey1, lockKey2);
        ensureOperationLockExists(id);
        entityManager.clear();
        return executeInTx(() -> {
            var acquiredLock = entityManager.find(OperationLock.class, id, LockModeType.PESSIMISTIC_WRITE);
            if(acquiredLock == null) {
                throw new FailedToAcquireLockException();
            }
            return operation.get();
        });
    }

    private void ensureOperationLockExists(OperationLockId id) {
        try {
            executeInTx(() -> {
                OperationLock lock = new OperationLock(id);
                entityManager.persist(lock);
                return null;
            });
        } catch (Exception ex) {
            logger.warn(ex);
        }
    }

    private <T> T executeInTx(Supplier<T> supplier) {
        try {
            userTransaction.begin();
            var result = supplier.get();
            userTransaction.commit();
            return result;
        } catch (Exception ex) {
            try {
                userTransaction.rollback();
            } catch (SystemException e) {
                logger.warn(e);
            }
            throw new RuntimeException(ex);
        }
    }

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperationLock {
        @EmbeddedId
        private OperationLockId id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class OperationLockId implements Serializable {
        private String lockKey1;
        private String lockKey2;
    }

    public static class FailedToAcquireLockException extends RuntimeException { }
}
