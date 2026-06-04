package concept.com.example.club.core.registration.model;

import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.registration.enumeration.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registrations")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user", "event"})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    //receber uma mensagem do porque da desistencia do evento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;


}
