package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.domain.OperationLocker;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SpringOperationLocker implements OperationLocker {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        return null;
    }
}
