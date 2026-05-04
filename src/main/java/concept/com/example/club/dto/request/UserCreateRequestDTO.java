package concept.com.example.club.dto.request;

import concept.com.example.club.enumeration.Plan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserCreateRequestDTO {
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

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    String password;

    @NotNull(message = "O plano deve ser selecionado")
    Plan plan;

    @Pattern(regexp = "^https?://.*", message = "Avatar deve ser uma URL válida")
    String avatar; // Pode ser opcional ou ter uma URL padrão

    @NotNull(message = "A preferencia deve ser informada")
    private List<String> preference;

    @NotNull(message = "O hobby deve ser informado")
    private List<String> hobbies;

}
