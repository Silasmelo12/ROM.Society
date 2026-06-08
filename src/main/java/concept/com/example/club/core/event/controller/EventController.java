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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDTO> create(
            @Valid @RequestPart("event") EventCreateRequestDTO dto,
            @RequestPart(value = "banner", required = false) MultipartFile bannerImage) {
        // Validação de segurança básica: É mesmo uma imagem?
        if (bannerImage != null && !bannerImage.isEmpty()) {
            if (bannerImage.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().build();
            }
            String contentType = bannerImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }
        }

        EventResponseDTO eventResponseDTO = eventService.createEvent(dto, bannerImage);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(eventResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(eventResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10) Pageable page) {
        Page<EventResponseDTO> responseDTOS = eventService.findAll(page);
        return ResponseEntity.ok(responseDTOS);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<Page<EventResponseDTO>> findMyAllowedEvents(
            @PageableDefault(page = 0, size = 10) Pageable page) {
        Page<EventResponseDTO> responseDTOS = eventService.findMyAllowedEvents(page);
        return ResponseEntity.ok(responseDTOS);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/{userId}")
    public ResponseEntity<EventResponseDTO> findByIdAllowedForUser(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findByIdAllowedForUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable String id,
            @Valid @RequestBody EventUpdateRequestDTO dto) {
        EventResponseDTO response = eventService.update(dto, id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
