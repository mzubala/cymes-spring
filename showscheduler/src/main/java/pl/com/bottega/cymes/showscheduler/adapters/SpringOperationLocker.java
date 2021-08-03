package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
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

@Component
public class SpringOperationLocker implements OperationLocker {

    private Logger logger = Logger.getLogger(SpringOperationLocker.class);

    private TransactionTemplate transactionTemplate;

    @Override
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        return null;
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
