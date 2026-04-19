package concept.com.example.club.repository;

import concept.com.example.club.model.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HobbyRepository extends JpaRepository<Hobby,String>  {

    List<Hobby> findByNameIn(List<String> names);
}
