package concept.com.example.club.repository;

import concept.com.example.club.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByActiveTrue();
    //User findByIdByActiveTrue(String id);

}
