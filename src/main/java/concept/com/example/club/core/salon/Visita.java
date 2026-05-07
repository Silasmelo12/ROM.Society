package concept.com.example.club.core.salon;

import concept.com.example.club.core.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
