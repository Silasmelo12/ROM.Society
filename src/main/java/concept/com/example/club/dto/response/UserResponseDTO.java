package concept.com.example.club.dto.response;

import concept.com.example.club.enumeration.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String id;

    private String name;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private Plan plan;

    private String avatar;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}