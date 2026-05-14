package concept.com.example.club.core.event.controller;

import concept.com.example.club.core.event.dto.EventCreateRequestDTO;
import concept.com.example.club.core.event.dto.EventResponseDTO;
import concept.com.example.club.core.event.dto.EventUpdateRequestDTO;
import concept.com.example.club.core.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "Events", description = "Endpoints para gerenciamento de eventos do clube")
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cria um novo evento", description = "Restrito a usuários com a role ADMIN.")
    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventCreateRequestDTO dto){
        EventResponseDTO eventResponseDTO = eventService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(eventResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @Operation(summary = "Lista todos os eventos de forma paginada", description = "Restrito a ADMIN e SUPERADMIN.")
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10)Pageable page){
        Page<EventResponseDTO> responseDTOS = eventService.findAll(page);
        return ResponseEntity.ok(responseDTOS);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lista os eventos permitidos do usuário atual", description = "Qualquer usuário autenticado pode acessar.")
    @GetMapping("/me")
    public ResponseEntity<Page<EventResponseDTO>> findMyAllowedEvents(
            @PageableDefault(page = 0, size = 10)Pageable page){
        Page<EventResponseDTO> responseDTOS = eventService.findMyAllowedEvents(page);
        return ResponseEntity.ok(responseDTOS);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @Operation(summary = "Busca evento por ID", description = "Restrito a ADMIN e SUPERADMIN.")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable String id){
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Busca evento específico do usuário atual por ID")
    @GetMapping("/me/{id}")
    public ResponseEntity<EventResponseDTO> findByIdAllowedForUser(@PathVariable String id){
        return ResponseEntity.ok(eventService.findByIdAllowedForUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualiza os dados de um evento existente", description = "Restrito a usuários com a role ADMIN.")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable String id, @Valid @RequestBody EventUpdateRequestDTO dto){
        EventResponseDTO response = eventService.update(dto,id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Exclui logicamente ou fisicamente um evento", description = "Restrito a usuários com a role ADMIN.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
