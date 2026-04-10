package concept.com.example.club.dto.request;

import concept.com.example.club.enumeration.Plan;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateRequestDTO {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan minimumPlan;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String category;

}
