package pl.com.bottega.cymes.showscheduler.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import pl.com.bottega.cymes.showscheduler.Resources;

import java.io.File;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;

class DeploymentFactory {
    static Archive<?> createTestArchive() {
        return ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addPackages(true, Resources.class.getPackage())
            .addAsResource("application.yml")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("show-scheduler-ds.xml")
            .addAsWebInfResource("beans.xml")
            .addAsLibraries(resolver().loadPomFromFile("pom.xml").resolve(
                "org.assertj:assertj-core",
                "commons-lang:commons-lang",
                "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml",
                "com.github.tomakehurst:wiremock-jre8-standalone",
                "com.netflix.hystrix:hystrix-core"
            ).withTransitivity().as(File.class));
    }

    static WireMockRule wireMockRule() {
        return new WireMockRule(options().port(8888).httpsPort(8889));
    }
}
