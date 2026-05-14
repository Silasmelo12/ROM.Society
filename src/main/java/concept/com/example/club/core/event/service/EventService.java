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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    @Transactional
    public EventResponseDTO create(EventCreateRequestDTO dto) {
        Event event = eventMapper.toEvent(dto);
        event.setAvailableSpots(dto.getCapacity());
        event.setActive(true);
        EventResponseDTO response = eventMapper.toEventResponseDTO(eventRepository.save(event));
        return response;
    }

    @Transactional
    public EventResponseDTO update(EventUpdateRequestDTO dto, String id) {
        log.info("Evento consultado: {}", id);
        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado com o id: " + id));

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

        Page<Event> eventsPage = eventRepository.findByActiveTrueAndAllowedPlansContaining(userLogado.getPlan(), pageable);
        return eventsPage.map(eventMapper::toEventResponseDTO);
    }

    public EventResponseDTO findById(String id) {
        Event response = eventRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new EventNotFoundException("Evento não encontrado para este id."));
        return eventMapper.toEventResponseDTO(response);
    }

    public EventResponseDTO findByIdAllowedForUser(String id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no token."));

        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado para este id."));

        if (!event.getAllowedPlans().contains(userLogado.getPlan())) {
            throw new RuntimeException("O seu plano atual não dá acesso a este evento exclusivo.");
        }

        return eventMapper.toEventResponseDTO(event);
    }

    @Transactional
    public void delete(String id) {
        Event event = eventRepository.findByIdAndActiveTrue(id).orElseThrow(
                ()->new EventNotFoundException("Evento não encontrado com o id: "+id)
        ); // Verifica se o evento existe antes de tentar deletar
        event.setActive(false);
        eventRepository.save(event);
    }
}
