package pl.com.bottega.cymes.showscheduler.adapters;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.showscheduler.domain.OperationLocker;

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
