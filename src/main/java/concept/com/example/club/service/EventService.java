package concept.com.example.club.service;

import concept.com.example.club.dto.request.EventCreateRequestDTO;
import concept.com.example.club.dto.request.EventUpdateRequestDTO;
import concept.com.example.club.dto.response.EventResponseDTO;
import concept.com.example.club.mapper.EventMapper;
import concept.com.example.club.model.Event;
import concept.com.example.club.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventResponseDTO create(EventCreateRequestDTO dto) {
        Event event = eventMapper.toEvent(dto);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        event.setAvailableSpots(5);
        log.info("Evento criado: {}", event.getCategory());
        EventResponseDTO response = eventMapper.toEventResponseDTO(eventRepository.save(event));
        log.info("Evento criado: {}", response.getSpeaker());
        return response;
    }

    public EventResponseDTO update(EventUpdateRequestDTO dto, String id) {
        log.info("Evento consultado: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com o id: " + id));

        eventMapper.updateEntityFromDto(dto, event);
        event.setUpdatedAt(LocalDateTime.now());
        return eventMapper.toEventResponseDTO(eventRepository.save(event));
    }


    public List<EventResponseDTO> findAll() {
        return eventMapper.toEventResponseDTO(eventRepository.findAll());

    }

    public EventResponseDTO findById(String id) {
        Event response = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Evento não encontrado para este id."));
        return eventMapper.toEventResponseDTO(response);
    }

    public void delete(String id) {
        eventRepository.deleteById(id);
    }
}
