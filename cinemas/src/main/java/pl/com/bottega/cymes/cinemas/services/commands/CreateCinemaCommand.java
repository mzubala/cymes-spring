package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

@Data
public class CreateCinemaCommand extends UserCommand {

    private String name;

    private String city;
}
