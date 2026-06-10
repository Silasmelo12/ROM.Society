package concept.com.example.club.core.salon.controller;

import concept.com.example.club.core.salon.dto.SalonCreateRequestDTO;
import concept.com.example.club.core.salon.dto.SalonResponseDTO;
import concept.com.example.club.core.salon.dto.SalonUpdateRequestDTO;
import concept.com.example.club.core.salon.service.SalonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping
    public ResponseEntity<SalonResponseDTO> create(@Valid @RequestBody SalonCreateRequestDTO dto) {
        SalonResponseDTO response = salonService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // superadmin, admin, concierge podem acessar
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'CONCIERGE')")
    @GetMapping
    public ResponseEntity<Page<SalonResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<SalonResponseDTO> salons = salonService.findAll(pageable);
        return ResponseEntity.ok(salons);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'CONCIERGE')")
    @GetMapping("/{salonId}")
    public ResponseEntity<SalonResponseDTO> findById(@PathVariable String salonId) {
        SalonResponseDTO response = salonService.findById(salonId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'CONCIERGE')")
    @GetMapping("/totem/{totemIdentifier}")
    public ResponseEntity<SalonResponseDTO> findByTotemIdentifier(@PathVariable String totemIdentifier) {
        SalonResponseDTO response = salonService.findByTotemIdentifier(totemIdentifier);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PutMapping("/{salonId}")
    public ResponseEntity<SalonResponseDTO> update(@PathVariable String salonId,
                                                   @Valid @RequestBody SalonUpdateRequestDTO dto) {
        SalonResponseDTO response = salonService.update(salonId, dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{salonId}")
    public ResponseEntity<Void> delete(@PathVariable String salonId) {
        salonService.delete(salonId);
        return ResponseEntity.noContent().build();
    }
}
