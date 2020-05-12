package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateCinemaHallCommand extends UserCommand {
    @NotNull
    private Long cinemaId;

    @NotBlank
    private String name;

    @NotEmpty
    @Valid
    private List<RowDto> layout;
}
