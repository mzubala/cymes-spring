package pl.com.bottega.ecom.infrastructure;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
class PersistentCommand {
    @Id
    private UUID id;
    private UUID userId;
    private Instant when;
    private String payload;
}
