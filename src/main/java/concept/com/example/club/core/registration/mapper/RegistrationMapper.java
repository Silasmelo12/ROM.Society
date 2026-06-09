package concept.com.example.club.core.registration.mapper;

import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper  {

    Registration toRegistration(RegistrationCreateRequestDTO dto);

    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "event.id",target = "eventId")
    RegistrationResponseDTO toRegistrationResponseDTO(Registration registration);

}
