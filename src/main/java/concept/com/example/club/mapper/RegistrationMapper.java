package concept.com.example.club.mapper;

import concept.com.example.club.dto.request.RegistrationCreateRequestDTO;
import concept.com.example.club.dto.response.RegistrationResponseDTO;
import concept.com.example.club.model.Registration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationMapper  {

    Registration toRegistration(RegistrationCreateRequestDTO dto);

    RegistrationResponseDTO toRegistrationResponseDTO(Registration registration);

}
