package concept.com.example.club.core.checkin.repository;

import concept.com.example.club.core.checkin.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin,String> {
}
