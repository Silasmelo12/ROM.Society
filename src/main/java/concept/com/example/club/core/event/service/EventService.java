package concept.com.example.club.core.event.service;

import concept.com.example.club.common.exception.EventNotFoundException;
import concept.com.example.club.common.exception.InvalidCapacityException;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.event.dto.EventCreateRequestDTO;
import concept.com.example.club.core.event.mapper.EventMapper;
import concept.com.example.club.core.event.dto.EventResponseDTO;
import concept.com.example.club.core.event.dto.EventUpdateRequestDTO;
import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.event.repository.EventRepository;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import concept.com.example.club.integration.storage.service.GcsStorageService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import concept.com.example.club.common.exception.PlanNotAllowedException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final GcsStorageService storageService;

    @Transactional
    public EventResponseDTO createEvent(EventCreateRequestDTO dto, MultipartFile bannerImage) {
        String imageUrl = storageService.uploadBanner(bannerImage);
        Event event = eventMapper.toEvent(dto);
        event.setAvailableSpots(dto.getCapacity());
        event.setActive(true);
        event.setImage(imageUrl);
        EventResponseDTO response = eventMapper.toEventResponseDTO(eventRepository.save(event));
        return response;
    }

    @Transactional
    public EventResponseDTO update(EventUpdateRequestDTO dto, String id) {
        log.info("Evento consultado: {}", id);
        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado com o userId: " + id));

        int newAvailableSpots = event.getAvailableSpots() + (dto.getCapacity() - event.getCapacity());
        if (newAvailableSpots < 0) {
            throw new InvalidCapacityException("A nova capacidade é menor do que o número de vagas já ocupadas.");
        }
        eventMapper.updateEntityFromDto(dto, event);
        event.setAvailableSpots(newAvailableSpots);
        event.setUpdatedAt(LocalDateTime.now());
        return eventMapper.toEventResponseDTO(eventRepository.save(event));
    }

    public Page<EventResponseDTO> findAll(Pageable pageable) {
        Page<Event> eventsPage = eventRepository.findByActiveTrue(pageable);
        return eventsPage.map(event -> eventMapper.toEventResponseDTO(event));
    }

    public Page<EventResponseDTO> findMyAllowedEvents(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no token."));

        log.info("Buscando eventos permitidos para o usuario: {} com o plano: {}", userEmail, userLogado.getPlan());
        Page<Event> eventsPage = eventRepository.findByActiveTrueAndAllowedPlansContaining(userLogado.getPlan(),
                pageable);
        log.info("Total de eventos encontrados pelo banco: {}", eventsPage.getTotalElements());

        return eventsPage.map(eventMapper::toEventResponseDTO);
    }

    public EventResponseDTO findById(String id) {
        Event response = eventRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new EventNotFoundException("Evento não encontrado para este userId."));
        return eventMapper.toEventResponseDTO(response);
    }

    public EventResponseDTO findByIdAllowedForUser(String id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no token."));

        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado para este userId."));

        if (!event.getAllowedPlans().contains(userLogado.getPlan())) {
            throw new PlanNotAllowedException("O seu plano atual não dá acesso a este evento exclusivo.");
        }

        return eventMapper.toEventResponseDTO(event);
    }

    @Transactional
    public void delete(String id) {
        Event event = eventRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new EventNotFoundException("Evento não encontrado com o userId: " + id)); // Verifica se o evento
                                                                                            // existe antes de tentar
                                                                                            // deletar
        event.setActive(false);
        eventRepository.save(event);
    }
}
