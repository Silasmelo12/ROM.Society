package concept.com.example.club.core.registration.dto;

import concept.com.example.club.core.registration.enumeration.RegistrationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCreateRequestDTO {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String userId;

    @NotBlank(message = "ID do evento é obrigatório")
    private String eventId;

    @NotNull(message = "Status é obrigatório")
    private RegistrationStatus status;

}
