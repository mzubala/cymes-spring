package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SuspensionDto {
    private Long id;
    private Instant from;
    private Instant to;
}
