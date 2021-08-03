package pl.com.bottega.cymes.showscheduler.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import pl.com.bottega.cymes.showscheduler.ShowSchedulerApp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(PostgresDBExtension.class)
@SpringBootTest(
        classes = {ShowSchedulerApp.class},
        properties = {"application.environment=integration"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration
public @interface IntegrationTest {
}
