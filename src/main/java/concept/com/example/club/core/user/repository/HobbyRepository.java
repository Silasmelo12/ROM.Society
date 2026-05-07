package concept.com.example.club.core.user.repository;

import concept.com.example.club.core.user.model.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HobbyRepository extends JpaRepository<Hobby,String>  {

    List<Hobby> findByNameIn(List<String> names);
}
