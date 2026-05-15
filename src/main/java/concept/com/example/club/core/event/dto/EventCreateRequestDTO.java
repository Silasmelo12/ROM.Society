package concept.com.example.club.core.event.dto;

import concept.com.example.club.core.user.enumeration.Plan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class EventCreateRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data deve ser no futuro")
    private LocalDateTime dateTime;

    @NotBlank(message = "Local é obrigatório")
    private String location;

    private String speaker;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    private Integer capacity;

    @NotNull(message = "Plano permitido é obrigatório")
    private Set<Plan> allowedPlans= new HashSet<>();

    @NotBlank(message = "Categoria é obrigatória")
    private String category;
}