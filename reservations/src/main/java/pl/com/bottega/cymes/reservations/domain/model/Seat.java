package pl.com.bottega.cymes.reservations.domain.model;

import lombok.Value;

@Value
public class Seat {
    Integer rowNumber;
    Integer seatNumber;
}
