package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import java.util.List;

@Data
public class UpdateCinemaHallCommand extends UserCommand {
    private Long cinemaHallId;

    private List<RowDto> layout;
}
