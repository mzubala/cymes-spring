package pl.com.bottega.cymes.showscheduler.adapters.configuration;

import io.netty.channel.ChannelOption;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(value = {ClientsProperties.class})
public class ClientsConfiguration {
    @Bean
    public WebClient cinemasClient(ClientsProperties clientsProperties) {
        return buildClient(clientsProperties.getCinemas());
    }

    @Bean
    public WebClient moviesClient(ClientsProperties clientsProperties) {
        return buildClient(clientsProperties.getMovies());
    }

    @Bean
    public RestTemplate cinemasRestTemplate(ClientsProperties clientsProperties) {
        return buildRestTemplate(clientsProperties.getCinemas());
    }

    @Bean
    public RestTemplate moviesRestTemplate(ClientsProperties clientsProperties) {
        return buildRestTemplate(clientsProperties.getMovies());
    }

    private WebClient buildClient(ClientProperties properties) {
        HttpClient client = HttpClient
                .create()
                .responseTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeout());
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeader("Content-type", MediaType.APPLICATION_JSON.toString())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }

    private RestTemplate buildRestTemplate(ClientProperties properties) {
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
            .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
            .defaultHeader("Content-type", MediaType.APPLICATION_JSON.toString())
            .rootUri(properties.getUrl())
            .build();
    }
}

@ConfigurationProperties(prefix = "clients")
@Data
class ClientsProperties {
    private ClientProperties movies;
    private ClientProperties cinemas;
}

@Data
class ClientProperties {
    private String url;
    private Integer connectionTimeout = 100;
    private Integer readTimeout = 500;
}
