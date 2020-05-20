package pl.com.bottega.cymes.showscheduler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.ShowResource.ScheduleShowRequest;
import pl.com.bottega.cymes.showscheduler.domain.Movie;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static pl.com.bottega.cymes.showscheduler.integration.DeploymentFactory.wireMockRule;

@RunWith(Arquillian.class)
public class ShowResourceTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    @Rule
    public WireMockRule wireMockRule = wireMockRule();

    private MovieAbility movieAbility;

    private CinemaAbility cinemaAbility;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        movieAbility = new MovieAbility(wireMockRule);
        cinemaAbility = new CinemaAbility(wireMockRule);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @RunAsClient
    public void schedulesShow(@ArquillianResource URL contextPath) {
        // given
        var scheduleShowRequest = new ScheduleShowRequest(UUID.randomUUID(), 1L, 2L, 3L, new Date());
        cinemaAbility.cinemaSuspensionCheckReturns(scheduleShowRequest.getCinemaId(), false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(scheduleShowRequest.getCinemaHallId(), false);
        movieAbility.stubMovie(new Movie(scheduleShowRequest.getMovieId(), 120));

        // when
        var response = scheduleShow(contextPath, scheduleShowRequest);

        assertThat(response.getStatus()).isEqualTo(204);
    }

    @SneakyThrows
    private Response scheduleShow(URL contextPath, ScheduleShowRequest request) {
        var client = ClientBuilder.newClient();
        try {
            var uri = UriBuilder.fromUri(contextPath.toURI()).path("/rest/shows").port(8080).build();
            return client.target(uri).request().post(Entity.entity(objectMapper.writeValueAsString(request), MediaType.APPLICATION_JSON));
        } finally {
            client.close();
        }
    }
}
