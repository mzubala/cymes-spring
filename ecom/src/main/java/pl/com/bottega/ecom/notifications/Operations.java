package pl.com.bottega.ecom.notifications;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Component
@Log
class Operations {

    @Async
    @SneakyThrows
    public CompletableFuture<String> operation1() {
        log.info("Operation 1");
        Thread.sleep(500);
        return CompletableFuture.completedFuture("ala");
    }


    @Async
    @SneakyThrows
    public CompletableFuture<Long> operation2() {
        log.info("Operation 2");
        Thread.sleep(1000);
        return CompletableFuture.completedFuture(100L);
    }

    @Async
    @SneakyThrows
    public CompletableFuture<BigDecimal> operation3() {
        log.info("Operation 3");
        Thread.sleep(1200);
        return CompletableFuture.completedFuture(BigDecimal.TEN);
    }
}
