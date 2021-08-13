package pl.com.bottega.cymes.showscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.com.bottega.cymes.showscheduler.adapters.configuration.SpringShowSchedulerConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({SpringShowSchedulerConfiguration.class})
public class ShowSchedulerApp {
    public static void main(String[] args) {
        SpringApplication.run(ShowSchedulerApp.class, args);
    }
}
