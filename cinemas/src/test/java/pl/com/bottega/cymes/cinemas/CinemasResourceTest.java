package pl.com.bottega.cymes.cinemas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.cinemas.dataaccess.model.PersistentCommand;
import pl.com.bottega.cymes.cinemas.resources.CinemaResource;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;


@RunWith(Arquillian.class)
public class CinemasResourceTest {

    private static final String REST_ROOT = "rest";

    @Inject
    private CinemaResource cinemaResource;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addPackages(true, CinemasResourceTest.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("cinemas-ds.xml")
            .addAsWebInfResource("beans.xml")
            .addAsLibraries(resolver().loadPomFromFile("pom.xml").resolve("org.assertj:assertj-core", "commons-lang:commons-lang")
                .withTransitivity().as(File.class));
    }

    @Test
    public void createsAndReturnsCinemas() {
        // when
        var request = new CreateCinemaRequest();
        request.setCity("Warszawa");
        request.setName("Arkadia" + UUID.randomUUID().toString());
        cinemaResource.create(request);
        var cinemas = cinemaResource.getAll();

        // then
        assertThat(cinemas).anyMatch((cinema) -> cinema.getCity().equals(request.getCity()) &&
            cinema.getName().equals(request.getName()) &&
            cinema.getId() != null
        );
    }

    @Test
    @RunAsClient
    public void returnsBadRequestWhenCreateCinemaRequestIsInvalid(@ArquillianResource URL contextPath) throws URISyntaxException {
        // when
        var response = createCinema(contextPath, "{}");

        // then
        assertThat(response.getStatusInfo()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void storesCreateCinemaCommand() {
        //when
        var request = new CreateCinemaRequest();
        request.setCity("Warszawa");
        request.setName("Arkadia" + UUID.randomUUID().toString());
        cinemaResource.create(request);

        // then
        var persistentCommands = entityManager.createQuery("FROM PersistentCommand pc", PersistentCommand.class).getResultList();
        assertThat(persistentCommands).anyMatch(cmd ->
            cmd.getType().equals("CreateCinemaCommand") &&
                cmd.getContent().contains("Warszawa") &&
                cmd.getContent().contains(request.getName())
        );
    }

    @Test
    public void doesNotAllowToDuplicateCinemas(@ArquillianResource URL contextPath) throws Exception {
        // given
        var request = new CreateCinemaRequest();
        request.setCity("Warszawa");
        request.setName("Arkadia" + UUID.randomUUID().toString());
        var requestString = new ObjectMapper().writeValueAsString(request);

        // when
        var firstResponse = createCinema(contextPath, requestString);
        var secondResponse = createCinema(contextPath, requestString);

        // then
        assertThat(firstResponse.getStatus()).isEqualTo(200);
        assertThat(secondResponse.getStatus()).isEqualTo(409);
    }

    private Response createCinema(@ArquillianResource URL contextPath, String requestJson) throws URISyntaxException {
        var client = ClientBuilder.newClient();
        try {
            var uri = UriBuilder.fromUri(contextPath.toURI()).path(REST_ROOT + "/cinemas").port(8080).build();
            return client.target(uri).request().post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));
        } finally {
            client.close();
        }
    }
}
