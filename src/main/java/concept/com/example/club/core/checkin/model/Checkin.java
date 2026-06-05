package concept.com.example.club.core.checkin.model;

import concept.com.example.club.core.checkin.enumeration.StatusCheckin;
import concept.com.example.club.core.salon.model.Salon;
import concept.com.example.club.core.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user"})
@EntityListeners(AuditingEntityListener.class)
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private StatusCheckin status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id")
    private Salon salon;
}
