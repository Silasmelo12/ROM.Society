package concept.com.example.club.core.registration.controller;

import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.service.RegistrationService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/events/{eventID}/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @PathVariable String eventID){
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.registerUserToEvent(eventID));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/events/{eventID}/registrations")
    public ResponseEntity<Page<RegistrationResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10)Pageable pageable){
        Page<RegistrationResponseDTO> responseDTOS = registrationService.findAll(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/registrations/me")
    public ResponseEntity<Page<RegistrationResponseDTO>> findMyRegistrations(
            @PageableDefault(page = 0, size = 10)Pageable pageable){
        Page<RegistrationResponseDTO> responseDTOS = registrationService.findMyRegistrations(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PatchMapping("/registrations/{registrationId}/cancel")
    public ResponseEntity<RegistrationResponseDTO> cancelRegistration(@PathVariable String registrationId) {
        RegistrationResponseDTO response = registrationService.cancelRegistration(registrationId);
        return ResponseEntity.ok(response);
    }
}
