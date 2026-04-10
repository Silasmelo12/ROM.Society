package concept.com.example.club.mapper;

import concept.com.example.club.dto.request.EventCreateRequestDTO;
import concept.com.example.club.dto.request.EventUpdateRequestDTO;
import concept.com.example.club.dto.response.EventResponseDTO;
import concept.com.example.club.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {


    Event toEvent(EventCreateRequestDTO dto);

    EventResponseDTO toEventResponseDTO(Event event);

    void updateEntityFromDto(EventUpdateRequestDTO dto, @MappingTarget Event event);

    List<EventResponseDTO> toEventResponseDTO(List<Event> events);
}
