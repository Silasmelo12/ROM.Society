package concept.com.example.club.model;

import concept.com.example.club.enumeration.Plan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan minimumPlan;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
