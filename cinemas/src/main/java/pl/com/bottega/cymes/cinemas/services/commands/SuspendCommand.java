package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

import java.time.Instant;

@Data
public class SuspendCommand extends UserCommand {
    private Long id;

    private Instant from;

    private Instant until;
}
