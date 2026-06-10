package concept.com.example.club.core.salon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SalonCreateRequestDTO {

    @NotBlank(message = "O identificador do totem é obrigatório")
    private String totemIdentifier;
}
