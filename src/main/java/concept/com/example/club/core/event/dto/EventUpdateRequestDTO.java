package concept.com.example.club.core.event.dto;

import concept.com.example.club.core.user.enumeration.Plan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data deve ser no futuro")
    private LocalDateTime dateTime;

    @NotBlank(message = "Local é obrigatório")
    private String location;

    @NotBlank(message = "Palestrante é obrigatório")
    private String speaker;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    private Integer capacity;

    @NotNull(message = "Plano mínimo é obrigatório")
    private Plan minimumPlan;

    @NotBlank(message = "Imagem é obrigatória")
    @Pattern(regexp = "^https?://.*", message = "Imagem deve ser uma URL válida")
    private String image;

    @NotBlank(message = "Categoria é obrigatória")
    private String category;
}