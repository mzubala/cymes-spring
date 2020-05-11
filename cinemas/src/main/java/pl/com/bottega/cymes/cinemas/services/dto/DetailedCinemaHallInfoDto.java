package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailedCinemaHallInfoDto {
    private Long id;
    private String name;
    private Integer capacity;
    private List<RowDto> rows;
}
