package concept.com.example.club.core.checkin.repository;

import concept.com.example.club.core.checkin.model.Checkin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin,String> {


    Page<Checkin> findAll(Pageable pageable);
    Page<Checkin> findBySalonId(String salonId, Pageable pageable);

}
