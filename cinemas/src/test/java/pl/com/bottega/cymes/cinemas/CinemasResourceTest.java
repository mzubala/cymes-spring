package pl.com.bottega.cymes.cinemas;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.cinemas.resources.request.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.services.dto.BasicCinemaInfoDto;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class CinemasResourceTest {

    private static final String REST_ROOT = "rest";

    @Autowired
    private CinemasClient cinemasClient;

    @Test
    public void createsAndReturnsCinemas() {
        // when
        var request = new CreateCinemaRequest();
        request.setCity("Warszawa");
        request.setName("Arkadia");
        var createCinemaResponse = cinemasClient.createCinema(request);
        var getCinemasResponse = cinemasClient.getCinemas();

        // then
        createCinemaResponse.expectStatus().isOk();
        getCinemasResponse
                .expectStatus().isOk()
                .expectBodyList(BasicCinemaInfoDto.class)
                .consumeWith(results ->
                        assertThat(results.getResponseBody())
                                .hasSize(1)
                                .anyMatch(cinema -> cinema.getCity().equals("Warszawa") && cinema.getName().equals("Arkadia"))
                );
    }

    @Test
    public void returnsBadRequestWhenCreateCinemaRequestIsInvalid() {
        // when
        var request = new CreateCinemaRequest();
        request.setCity(null);
        request.setName("Arkadia");
        var createCinemaResponse = cinemasClient.createCinema(request);

        // then
        createCinemaResponse.expectStatus().isBadRequest();
    }
}
