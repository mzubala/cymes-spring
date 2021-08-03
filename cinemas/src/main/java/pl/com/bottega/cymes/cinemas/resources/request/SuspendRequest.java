package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

import java.time.Instant;

@Data
public class SuspendRequest {
    private Instant from;
    private Instant until;
}
