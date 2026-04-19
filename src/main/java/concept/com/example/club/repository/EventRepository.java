package concept.com.example.club.repository;

import concept.com.example.club.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByActiveTrue();

    Optional<Event> findByIdAndActiveTrue(String id);
}
