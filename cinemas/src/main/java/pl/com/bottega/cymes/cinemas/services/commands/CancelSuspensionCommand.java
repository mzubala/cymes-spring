package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

@Data
public class CancelSuspensionCommand extends UserCommand {
    private Long suspensionId;
}
