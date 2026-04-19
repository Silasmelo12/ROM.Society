package concept.com.example.club.controller;

import concept.com.example.club.dto.request.RegistrationCreateRequestDTO;
import concept.com.example.club.dto.response.RegistrationResponseDTO;
import concept.com.example.club.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegistrationCreateRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.create(dto));
    }
}
