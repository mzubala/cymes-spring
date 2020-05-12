package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateCinemaCommand extends UserCommand {

    @NotNull
    private String name;

    @NotNull
    private String city;
}
