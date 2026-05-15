package concept.com.example.club.core.event.repository;

import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.user.enumeration.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    Page<Event> findByActiveTrue(Pageable pageable);

    Optional<Event> findByIdAndActiveTrue(String id);

    @Query("SELECT e FROM Event e JOIN e.allowedPlans p WHERE e.active = true AND p = :plan")
    Page<Event> findByActiveTrueAndAllowedPlansContaining(@Param("plan") Plan plan, Pageable pageable);
}
