package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateCinemaRequest {
    @NotBlank
    private String city;
    @NotBlank
    private String name;
}
