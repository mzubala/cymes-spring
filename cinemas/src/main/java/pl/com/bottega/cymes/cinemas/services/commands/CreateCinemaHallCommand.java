package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.services.dto.RowDto;

import java.util.List;

@Data
public class CreateCinemaHallCommand extends UserCommand {
    private Long cinemaId;

    private String name;

    private List<RowDto> layout;
}
