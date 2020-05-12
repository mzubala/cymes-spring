package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class SuspendCommand extends UserCommand {
    @NotNull
    private Long id;

    @Future
    @NotNull
    private Instant from;

    @Future
    @NotNull
    private Instant until;
}
