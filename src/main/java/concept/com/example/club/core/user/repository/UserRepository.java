package concept.com.example.club.core.user.repository;

import concept.com.example.club.core.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByActiveTrue();

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    //User findByIdByActiveTrue(String userId);
    Page<User> findByActiveTrue(Pageable pageable);
    List<User> findByMarketingOptInTrueAndActiveTrue();

}
