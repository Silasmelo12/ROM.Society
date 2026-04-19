package concept.com.example.club.repository;

import concept.com.example.club.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByActiveTrue();

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    //User findByIdByActiveTrue(String id);

}
