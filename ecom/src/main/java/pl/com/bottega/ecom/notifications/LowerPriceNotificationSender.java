package pl.com.bottega.ecom.notifications;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.com.bottega.ecom.catalog.ProductPriceChangedEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
@Log
@RequiredArgsConstructor
class LowerPriceNotificationSender {

    private final Operations operations;

    @TransactionalEventListener
    @Async
    @SneakyThrows
    public void productPriceChanged(ProductPriceChangedEvent event) {
        log.info("Event received = " + event);
        var f1 = operations.operation1();
        var f2 = operations.operation2();
        var f3 = operations.operation3();
        CompletableFuture.allOf(f1, f2, f3).join();
        log.info(f1.get());
        log.info(f2.get().toString());
        log.info(f3.get().toString());
    }

}
