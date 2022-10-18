package pl.com.bottega.cymes.showscheduler.domain;

import java.util.function.Supplier;

public interface OperationLocker {
    <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation);

    default void lock(String lockKey1, String lockKey2, Runnable operation) {
        lock(lockKey1, lockKey2, () -> {
            operation.run();
            return null;
        });
    }
}
