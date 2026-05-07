package concept.com.example.club.core.registration.service;

import concept.com.example.club.core.event.service.EventService;
import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.mapper.RegistrationMapper;
import concept.com.example.club.core.registration.model.Registration;
import concept.com.example.club.core.registration.repository.RegistrationRepository;
import concept.com.example.club.core.user.service.UserService;
import concept.com.example.club.core.event.mapper.EventMapper;
import concept.com.example.club.core.user.mapper.UserMapper;
import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.event.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public RegistrationResponseDTO create(@Valid RegistrationCreateRequestDTO dto) {
        User user = userMapper.toUser(userService.findById(dto.getUserId()));
        Event event = eventMapper.toEvent(eventService.findById(dto.getEventId()));

        //verifica se o user já está incrito no evento
        if(registrationRepository.existsByUserIdAndEventId(dto.getUserId(), dto.getEventId())){
            throw new RuntimeException("Usuário já está inscrito no evento");
        }

        // verifica se há vagas disponíveis
        if(event.getAvailableSpots()<=0){
            throw new RuntimeException("Não há mais vaga no evento.");
        }

        Registration registration = registrationMapper.toRegistration(dto);
        registration.setRegistrationDate(java.time.LocalDateTime.now());
        registration.setUser(user);
        registration.setEvent(event);
        event.setAvailableSpots(event.getAvailableSpots()-1);
        eventRepository.save(event);
        RegistrationResponseDTO registrationResponseDTO = registrationMapper.toRegistrationResponseDTO(registrationRepository.save(registration));
        registrationResponseDTO.setEventId(event.getId());
        registrationResponseDTO.setUserId(user.getId());
        return registrationResponseDTO;
    }
}
