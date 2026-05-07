package concept.com.example.club.core.user.repository;

import concept.com.example.club.core.user.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference,String> {
    List<Preference> findByNameIn(List<String> names);
}
