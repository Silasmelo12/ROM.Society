package concept.com.example.club.core.registration.repository;

import concept.com.example.club.core.registration.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    boolean existsByUserIdAndEventId(String userId, String eventId);
}
