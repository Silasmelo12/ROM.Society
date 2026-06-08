package concept.com.example.club.core.registration.service;

import concept.com.example.club.common.exception.EventNotFoundException;
import concept.com.example.club.common.exception.PlanNotAllowedException;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.enumeration.RegistrationStatus;
import concept.com.example.club.core.registration.mapper.RegistrationMapper;
import concept.com.example.club.core.registration.model.Registration;
import concept.com.example.club.core.registration.repository.RegistrationRepository;
import concept.com.example.club.core.user.repository.UserRepository;
import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import concept.com.example.club.common.exception.AlreadyRegisteredException;
import concept.com.example.club.common.exception.ForbiddenAccessException;
import concept.com.example.club.common.exception.NoAvailableSpotsException;
import concept.com.example.club.common.exception.RegistrationNotFoundException;
import concept.com.example.club.integration.email.service.ResendEmailService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ResendEmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    @Transactional
    public RegistrationResponseDTO registerUserToEvent(String eventId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLogado = userRepository.findByEmail(userEmail).
                orElseThrow(()->new UserNotFoundException("Usuário não encontrado no token."));

        Event event = eventRepository.findByIdAndActiveTrue(eventId)
                .orElseThrow(()->new EventNotFoundException("Evento não encontrado."));

        validarAcessoAoEvento(userLogado,event);

        //verifica se o user já está incrito no evento
        if(registrationRepository.existsByUserIdAndEventId(userLogado.getId(), event.getId())){
            throw new AlreadyRegisteredException("Usuário já está inscrito no evento");
        }

        // verifica se há vagas disponíveis
        if(event.getAvailableSpots()<=0){
            throw new NoAvailableSpotsException("Não há mais vaga no evento.");
        }

        Registration registration = new Registration();
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setUser(userLogado);
        registration.setEvent(event);
        registration.setStatus(RegistrationStatus.REGISTERED);

        event.setAvailableSpots(event.getAvailableSpots()-1);
        eventRepository.save(event);
        emailService.sendRegistrationConfirmation(userLogado.getEmail(), userLogado.getName(), event.getTitle());

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

    @Transactional
    public RegistrationResponseDTO cancelRegistration(String registrationId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        // 1. Busca a inscrição
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Inscrição não encontrada."));

        // 2. Trava de segurança: garantir que só o dono da inscrição pode cancelar
        if (!registration.getUser().getId().equals(userLogado.getId())) {
            throw new ForbiddenAccessException("Acesso negado: Você não pode cancelar uma inscrição que não é sua.");
        }

        // 3. Valida se já foi cancelada
        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new AlreadyRegisteredException("Esta inscrição já encontra-se cancelada.");
        }

        // 4. Atualiza o status e devolve a vaga para o evento
        registration.setStatus(RegistrationStatus.CANCELLED);
        
        Event event = registration.getEvent();
        event.setAvailableSpots(event.getAvailableSpots() + 1);
        eventRepository.save(event);

        return registrationMapper.toRegistrationResponseDTO(registrationRepository.save(registration));
    }

    public Page<RegistrationResponseDTO> findAllByEventId(String eventID, Pageable pageable) {

        Page<Registration> page = registrationRepository.findAllByEventId(eventID,pageable);
        return page.map(registration -> registrationMapper.toRegistrationResponseDTO(registration));
    }
}
