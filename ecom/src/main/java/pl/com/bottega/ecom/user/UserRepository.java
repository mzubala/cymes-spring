package pl.com.bottega.ecom.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
