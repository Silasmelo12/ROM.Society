package concept.com.example.club.dto.request;

import concept.com.example.club.enumeration.Plan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email;

    @NotBlank(message = "Telefone é obrigatório")
    String phone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    LocalDate birthDate;

    @NotNull(message = "O plano deve ser selecionado")
    Plan plan;

    @Pattern(regexp = "^https?://.*", message = "Avatar deve ser uma URL válida")
    String avatar;

    @NotNull(message = "Status ativo é obrigatório")
    Boolean active;

}
