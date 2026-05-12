package concept.com.example.club.core.event.service;

import concept.com.example.club.common.exception.EventNotFoundException;
import concept.com.example.club.common.exception.InvalidCapacityException;
import concept.com.example.club.core.event.dto.EventCreateRequestDTO;
import concept.com.example.club.core.event.mapper.EventMapper;
import concept.com.example.club.core.event.dto.EventResponseDTO;
import concept.com.example.club.core.event.dto.EventUpdateRequestDTO;
import concept.com.example.club.core.event.model.Event;
import concept.com.example.club.core.event.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

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

    public EventResponseDTO findById(String id) {
        Event response = eventRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new EventNotFoundException("Evento não encontrado para este id."));
        return eventMapper.toEventResponseDTO(response);
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
