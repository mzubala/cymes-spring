package pl.com.bottega.cymes.reservations.domain.ports;

import lombok.Value;
import pl.com.bottega.cymes.reservations.domain.model.TicketKind;

import java.util.Map;
import java.util.UUID;

@Value
public class SelectTicketsCommand {
    UUID reservationId;
    Map<TicketKind, Integer> ticketsMap;
}
