package pl.com.bottega.cymes.cinemas;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.cinemas.resources.CinemaResource;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;


@RunWith(Arquillian.class)
public class CinemasResourceTest {

    private static final String REST_ROOT = "rest";

    @Inject
    private CinemaResource cinemaResource;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addPackages(true, CinemasResourceTest.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("cinemas-ds.xml")
            .addAsWebInfResource("beans.xml")
            .addAsLibraries(resolver().loadPomFromFile("pom.xml").resolve("org.assertj:assertj-core").withTransitivity().as(File.class));
    }

    @Test
    public void createsAndReturnsCinemas() {
        // when
        var request = new CreateCinemaRequest();
        request.setCity("Warszawa");
        request.setName("Arkadia");
        cinemaResource.create(request);
        var cinemas = cinemaResource.getAll();

        // then
        assertThat(cinemas).contains(BasicCinemaInfoDto.builder().id(1L)
            .city("Warszawa")
            .name("Arkadia")
            .build()
        );
    }

    @Test
    @RunAsClient
    public void returnsBadRequestWhenCreateCinemaRequestIsInvalid(@ArquillianResource URL contextPath) throws URISyntaxException {
        // when
        var client = ClientBuilder.newClient();
        var uri = UriBuilder.fromUri(contextPath.toURI()).path(REST_ROOT + "/cinemas").port(8080).build();
        var response = client.target(uri).request().post(Entity.entity("{}", MediaType.APPLICATION_JSON));

        // then
        assertThat(response.getStatusInfo()).isEqualTo(BAD_REQUEST);
        client.close();
    }
}
