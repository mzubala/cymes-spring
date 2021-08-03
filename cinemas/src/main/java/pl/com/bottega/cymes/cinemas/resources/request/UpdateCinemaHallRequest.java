package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateCinemaHallRequest {
    @NotEmpty
    @Valid
    private List<RowDto> layout;
}
