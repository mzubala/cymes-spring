package pl.com.bottega.cymes.showscheduler.domain;

import pl.com.bottega.cymes.showscheduler.domain.ports.OperationLocker;

import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FakeOperationLocker implements OperationLocker {

    private boolean errorOccurred;

    @Override
    public <T> T lock(String lockKey1, String lockKey2, Supplier<T> operation) {
        try {
            return operation.get();
        } catch (Exception ex) {
            errorOccurred = true;
            throw ex;
        }
    }

    void assertThatErrorOccuredDuringLockedOperation() {
        assertThat(errorOccurred).isTrue();
    }

    void assertThatNoErrorOccuredDuringLockedOperation() {
        assertThat(errorOccurred).isFalse();
    }
}
