package pl.com.bottega.ecom.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(
    indexes = {
        @Index(columnList = "email", unique = true)
    }
)
class User {
    @Id
    private UUID id;
    private String email;
    private String password;
}
