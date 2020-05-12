package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateCinemaHallCommand extends UserCommand {
    @NotNull
    private Long cinemaHallId;

    @Valid
    @NotEmpty
    private List<RowDto> layout;
}
