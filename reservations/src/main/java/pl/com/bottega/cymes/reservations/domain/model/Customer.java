package pl.com.bottega.cymes.reservations.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

public abstract class Customer {

    private Customer() {

    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    static class AnonymousCustomer extends Customer {
        String firstName;
        String lastName;
        String email;
        String phoneNumber;
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    static class RegisteredCustomer extends Customer {
        CustomerId customerId;
    }

    @Value
    static class CustomerId {
        UUID value;
    }

}
