package concept.com.example.club.core.event.mapper;

import concept.com.example.club.core.event.dto.EventResponseDTO;
import concept.com.example.club.core.event.dto.EventCreateRequestDTO;
import concept.com.example.club.core.event.dto.EventUpdateRequestDTO;
import concept.com.example.club.core.event.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {


    Event toEvent(EventCreateRequestDTO dto);

    Event toEvent(EventResponseDTO dto);

    EventResponseDTO toEventResponseDTO(Event event);

    void updateEntityFromDto(EventUpdateRequestDTO dto, @MappingTarget Event event);

    List<EventResponseDTO> toEventResponseDTO(List<Event> events);
}
