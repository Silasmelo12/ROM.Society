package RomConcept.com.example.Club.Rom.Concept.repository;

import RomConcept.com.example.Club.Rom.Concept.model.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
