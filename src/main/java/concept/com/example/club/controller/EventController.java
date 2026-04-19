package concept.com.example.club.controller;

import concept.com.example.club.dto.request.EventCreateRequestDTO;
import concept.com.example.club.dto.request.EventUpdateRequestDTO;
import concept.com.example.club.dto.response.EventResponseDTO;
import concept.com.example.club.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventCreateRequestDTO dto){
        EventResponseDTO eventResponseDTO = eventService.create(dto);
        return new ResponseEntity<>(eventResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> findAll(){
        List<EventResponseDTO> responseDTOS = eventService.findAll();
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@Valid @RequestBody EventUpdateRequestDTO dto, @PathVariable String id){
        EventResponseDTO response = eventService.update(dto,id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        eventService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
