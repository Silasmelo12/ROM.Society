package concept.com.example.club.dto.response;

import concept.com.example.club.enumeration.Plan;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private String speaker;
    private Integer capacity;
    private Integer availableSpots;
    private Plan minimumPlan;
    private String image;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
