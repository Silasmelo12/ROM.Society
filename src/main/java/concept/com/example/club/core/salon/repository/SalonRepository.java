package concept.com.example.club.core.salon.repository;

import concept.com.example.club.core.salon.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, String> {

    Optional<Salon> findByTotemIdentifier(String totemIdentifier);
}
