package pl.com.bottega.cymes.showscheduler.domain.ports;

import java.util.function.Supplier;

public interface OperationLocker {
    <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation);
}
