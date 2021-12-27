package pl.com.bottega.cymes.reservations.domain.model;

import pl.com.bottega.cymes.reservations.domain.model.Customer.CustomerId;
import pl.com.bottega.cymes.reservations.domain.model.Customer.RegisteredCustomer;

import java.util.UUID;

public class CustomerExample {
    static RegisteredCustomer aRegisteredCustomer() {
        return new RegisteredCustomer(new CustomerId(UUID.randomUUID()));
    }
}
