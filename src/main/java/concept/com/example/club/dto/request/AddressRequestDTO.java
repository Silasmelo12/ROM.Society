package concept.com.example.club.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        @NotBlank String cep,
        @NotBlank String number,
        @NotBlank String complement,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String country
) {
}
