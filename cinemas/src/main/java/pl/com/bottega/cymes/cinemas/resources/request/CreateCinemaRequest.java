package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

@Data
public class CreateCinemaRequest {
    private String city;
    private String name;
}
