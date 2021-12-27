package pl.com.bottega.cymes.reservations.domain.model;

public class InvalidReservationOperationException extends RuntimeException {
    InvalidReservationOperationException(String description) {
        super(description);
    }
}
