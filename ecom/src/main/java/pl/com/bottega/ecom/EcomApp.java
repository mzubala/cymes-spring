package pl.com.bottega.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
class EcomApp {
    public static void main(String[] args) {
        SpringApplication.run(EcomApp.class, args);
    }
}
