package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateCinemaHallRequest {
    @NotNull
    private Long cinemaId;

    @NotBlank
    private String name;

    @NotEmpty
    @Valid
    private List<RowDto> layout;
}
