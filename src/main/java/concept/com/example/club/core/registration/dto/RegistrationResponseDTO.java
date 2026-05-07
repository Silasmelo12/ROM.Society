package concept.com.example.club.core.registration.dto;

import concept.com.example.club.core.registration.enumeration.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {

    private String id;

    private String userId;

    private String eventId;

    private LocalDateTime registrationDate;

    private RegistrationStatus status;

    private Boolean interested;

}
