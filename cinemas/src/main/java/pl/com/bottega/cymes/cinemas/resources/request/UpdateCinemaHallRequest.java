package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateCinemaHallRequest {
    @NotEmpty
    @Valid
    private List<RowDto> layout;
}
