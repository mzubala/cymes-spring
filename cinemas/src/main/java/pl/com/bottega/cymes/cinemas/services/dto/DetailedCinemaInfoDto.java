package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailedCinemaInfoDto {
    private Long id;
    private String name;
    private String city;
    private List<BasicCinemaHallInfoDto> halls;
    private Boolean suspended;

    public DetailedCinemaInfoDto(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }
}
