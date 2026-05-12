package concept.com.example.club.core.user.dto;

import concept.com.example.club.core.user.enumeration.Plan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    private String name;

    private String phone;

    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate birthDate;

    private Plan plan;

    @Pattern(regexp = "^https?://.*", message = "Avatar deve ser uma URL válida")
    private String avatar;

    @Size(max = 3, message = "Você pode escolher no máximo 3 preferências")
    private List<String> preferences;

    @Size(max = 3, message = "Você pode escolher no máximo 3 hobbies")
    private List<String> hobbies;

    @Valid
    private AddressRequestDTO addressRequestDTO;

}
