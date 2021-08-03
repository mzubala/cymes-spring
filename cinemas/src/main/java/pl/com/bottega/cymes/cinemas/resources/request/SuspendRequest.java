package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
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
