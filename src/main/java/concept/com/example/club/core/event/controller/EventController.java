package concept.com.example.club.core.event.controller;

import concept.com.example.club.core.event.dto.EventCreateRequestDTO;
import concept.com.example.club.core.event.dto.EventResponseDTO;
import concept.com.example.club.core.event.dto.EventUpdateRequestDTO;
import concept.com.example.club.core.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventCreateRequestDTO dto){
        EventResponseDTO eventResponseDTO = eventService.create(dto);
        return new ResponseEntity<>(eventResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10)Pageable page){
        Page<EventResponseDTO> responseDTOS = eventService.findAll(page);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@Valid @RequestBody EventUpdateRequestDTO dto, @PathVariable String id){
        EventResponseDTO response = eventService.update(dto,id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        eventService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
