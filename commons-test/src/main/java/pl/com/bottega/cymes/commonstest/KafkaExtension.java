package pl.com.bottega.cymes.commonstest;

import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Log
public class KafkaExtension implements BeforeAllCallback {

    private KafkaContainer kafka;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("About to prepare kafka container");
        if(kafka == null) {
            log.info("Creating kafka container");
            kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        }
        if(!kafka.isRunning()) {
            log.info("Starting kafka container");
            kafka.start();
        }
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }
}
