package concept.com.example.club.repository;

import concept.com.example.club.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference,String> {
    List<Preference> findByNameIn(List<String> names);
}
