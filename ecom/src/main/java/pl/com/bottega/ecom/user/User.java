package pl.com.bottega.ecom.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
