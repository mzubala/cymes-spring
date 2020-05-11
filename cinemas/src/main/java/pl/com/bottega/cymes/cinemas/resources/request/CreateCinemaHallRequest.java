package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import java.util.List;

@Data
public class CreateCinemaHallRequest {
    private Long cinemaId;
    private String name;
    private List<RowDto> layout;
}
