package pl.com.bottega.ecom.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PersistentCommandRepository extends JpaRepository<PersistentCommand, UUID> {
}
