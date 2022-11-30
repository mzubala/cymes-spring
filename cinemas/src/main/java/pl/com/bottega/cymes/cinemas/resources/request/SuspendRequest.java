package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class SuspendRequest {
    @NotNull
    @Future
    private Instant from;

    @NotNull
    @Future
    private Instant until;
}
