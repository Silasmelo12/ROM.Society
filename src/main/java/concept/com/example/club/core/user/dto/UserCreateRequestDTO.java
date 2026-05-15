package concept.com.example.club.core.user.dto;

import concept.com.example.club.core.user.enumeration.Plan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserCreateRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    private String phone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate birthDate;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    @NotNull(message = "O plano deve ser selecionado")
    private Plan plan;

    @Pattern(regexp = "^https?://.*", message = "Avatar deve ser uma URL válida")
    private String avatar;

    @NotNull(message = "A preferência deve ser informada")
    @Size(max = 3, message = "Você pode escolher no máximo 3 preferências")
    private List<String> preferences;

    @NotNull(message = "O hobby deve ser informado")
    @Size(max = 3, message = "Você pode escolher no máximo 3 hobbies")
    private List<String> hobbies;

    @NotNull(message = "O endereço é obrigatório")
    @Valid
    private AddressRequestDTO addressRequestDTO;

    @NotNull
    private Boolean marketingOptIn;

}