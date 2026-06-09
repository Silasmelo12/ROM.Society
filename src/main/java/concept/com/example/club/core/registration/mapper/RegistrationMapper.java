package concept.com.example.club.core.registration.mapper;

import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper  {

    Registration toRegistration(RegistrationCreateRequestDTO dto);

    @Mapping(source = "user.userId",target = "userId")
    @Mapping(source = "event.userId",target = "eventId")
    RegistrationResponseDTO toRegistrationResponseDTO(Registration registration);

}
