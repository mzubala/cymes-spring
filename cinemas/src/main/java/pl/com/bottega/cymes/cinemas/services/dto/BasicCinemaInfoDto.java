package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicCinemaInfoDto {
    private Long id;
    private String name;
    private String city;
}
