package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelSuspensionCommand extends UserCommand {
    @NotNull
    private Long suspensionId;
}
