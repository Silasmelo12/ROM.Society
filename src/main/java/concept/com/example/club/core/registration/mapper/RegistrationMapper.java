package concept.com.example.club.core.registration.mapper;

import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.model.Registration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationMapper  {

    Registration toRegistration(RegistrationCreateRequestDTO dto);

    RegistrationResponseDTO toRegistrationResponseDTO(Registration registration);

}
