package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
