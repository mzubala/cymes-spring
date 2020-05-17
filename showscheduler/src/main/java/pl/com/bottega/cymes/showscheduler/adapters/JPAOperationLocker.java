package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.logging.Logger;
import pl.com.bottega.cymes.showscheduler.domain.OperationLocker;

import javax.annotation.Resource;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.function.Supplier;

public class JPAOperationLocker implements OperationLocker {

    @PersistenceContext
    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(JPAOperationLocker.class);

    @Resource
    private UserTransaction userTransaction;

    @Override
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        return null;
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
}
