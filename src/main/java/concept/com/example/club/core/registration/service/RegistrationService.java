package concept.com.example.club.core.registration.service;

import concept.com.example.club.common.exception.EventNotFoundException;
import concept.com.example.club.common.exception.PlanNotAllowedException;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.event.service.EventService;
import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.enumeration.RegistrationStatus;
import concept.com.example.club.core.registration.mapper.RegistrationMapper;
import concept.com.example.club.core.registration.model.Registration;
import concept.com.example.club.core.registration.repository.RegistrationRepository;
import concept.com.example.club.core.user.repository.UserRepository;
import concept.com.example.club.core.user.service.UserService;
import concept.com.example.club.core.event.mapper.EventMapper;
import concept.com.example.club.core.user.mapper.UserMapper;
import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.event.repository.EventRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationService.class);

    @Transactional
    public RegistrationResponseDTO create(@Valid String eventId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLogado = userRepository.findByEmail(userEmail).
                orElseThrow(()->new UserNotFoundException("Usuário não encontrado no token."));

        Event event = eventRepository.findByIdAndActiveTrue(eventId)
                .orElseThrow(()->new EventNotFoundException("Evento não encontrado."));

        validarAcessoAoEvento(userLogado,event);

        //verifica se o user já está incrito no evento
        if(registrationRepository.existsByUserIdAndEventId(userLogado.getId(), event.getId())){
            throw new RuntimeException("Usuário já está inscrito no evento");
        }

        // verifica se há vagas disponíveis
        if(event.getAvailableSpots()<=0){
            throw new RuntimeException("Não há mais vaga no evento.");
        }

        Registration registration = new Registration();
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setUser(userLogado);
        registration.setEvent(event);
        registration.setStatus(RegistrationStatus.REGISTERED);

        event.setAvailableSpots(event.getAvailableSpots()-1);
        eventRepository.save(event);

        RegistrationResponseDTO registrationResponseDTO = registrationMapper.toRegistrationResponseDTO(registrationRepository.save(registration));
        registrationResponseDTO.setEventId(event.getId());
        registrationResponseDTO.setUserId(userLogado.getId());
        return registrationResponseDTO;
    }

    public Page<RegistrationResponseDTO> findAll(Pageable pageable){

        Page<Registration> registrationsPage = registrationRepository.findAll(pageable);
        return registrationsPage.map(registration -> registrationMapper.toRegistrationResponseDTO(registration));
    }

    public Page<RegistrationResponseDTO> findMyRegistrations(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        // Busca apenas as inscrições deste usuário
        Page<Registration> page = registrationRepository.findAllByUserId(userLogado.getId(), pageable);
        return page.map(registrationMapper::toRegistrationResponseDTO);
    }

    public void validarAcessoAoEvento(User user, Event event) {
        // A trava de segurança exata: O plano do usuário está na lista VIP deste evento?
        if (!event.getAllowedPlans().contains(user.getPlan())) {
            log.warn("Tentativa de acesso negada. O plano {} não tem permissão para o evento {}.",
                    user.getPlan(), event.getAllowedPlans());
            throw new PlanNotAllowedException("O seu plano atual não dá acesso a este evento exclusivo.");
        }
    }
}
