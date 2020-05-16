package pl.com.bottega.cymes.showscheduler.integration;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import pl.com.bottega.cymes.showscheduler.Resources;

import java.io.File;

import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;

public class DeploymentFactory {
    public static Archive<?> createTestArchive() {
        return ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addPackages(true, Resources.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("show-scheduler-ds.xml")
            .addAsWebInfResource("beans.xml")
            .addAsLibraries(resolver().loadPomFromFile("pom.xml").resolve("org.assertj:assertj-core", "commons-lang:commons-lang")
                .withTransitivity().as(File.class));
    }
}
