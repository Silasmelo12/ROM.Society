package concept.com.example.club.core.registration.controller;

import concept.com.example.club.core.registration.dto.RegistrationCreateRequestDTO;
import concept.com.example.club.core.registration.dto.RegistrationResponseDTO;
import concept.com.example.club.core.registration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/events/{eventID}/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @PathVariable String eventID){
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.create(eventID));
    }
}
