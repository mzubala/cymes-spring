package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

@Data
public class BasicCinemaHallInfoDto {
    private Long id;
    private String name;
    private Integer capacity;
    private Boolean suspended;

    public BasicCinemaHallInfoDto(Long id, String name, Integer capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
}
