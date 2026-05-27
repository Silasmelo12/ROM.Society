package concept.com.example.club.core.event.dto;

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
    private String image;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
