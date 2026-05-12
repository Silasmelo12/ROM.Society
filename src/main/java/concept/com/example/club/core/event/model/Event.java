package concept.com.example.club.core.event.model;

import concept.com.example.club.core.user.enumeration.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
//@Version
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String speaker;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer availableSpots;

    //o evento só pode ficar visível para quem tem o plano mínimo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan minimumPlan;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;

    //@Version
    //private Long version;



}
