package pl.com.bottega.cymes.reservations.domain.model.model.application;

import lombok.Value;

import java.util.UUID;

@Value
public class SelectShowCommand {
    UUID reservationId;
    UUID showId;
}
