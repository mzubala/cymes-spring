package pl.com.bottega.cymes.cinemas.resources.request;

import lombok.Data;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.Instant;

@Data
public class SuspendRequest {
    @JsonbDateFormat("dd/MM/yyyy HH:mm")
    private Instant from;
    @JsonbDateFormat("dd/MM/yyyy HH:mm")
    private Instant until;
}
