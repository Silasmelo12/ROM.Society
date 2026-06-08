package concept.com.example.club.core.user.controller;

import concept.com.example.club.core.user.dto.UserCreateRequestDTO;
import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.core.user.dto.UserResponseDetailDTO;
import concept.com.example.club.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Concierge cria assinantes
    @PreAuthorize("hasRole('CONCIERGE')")
    @PostMapping("/assinantes")
    public ResponseEntity<UserResponseDTO> criarAssinante(
            @Valid @RequestBody UserCreateRequestDTO dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto, authentication));
    }

    // Admin cria concierge e barman
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/staff")
    public ResponseEntity<UserResponseDTO> criarStaff(
            @Valid @RequestBody UserCreateRequestDTO dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto, authentication));
    }

    // SuperAdmin cria admin
    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> criarAdmin(
            @Valid @RequestBody UserCreateRequestDTO dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto, authentication));
    }

    // profissionalmente, findAll() deve ter paginação
    @PreAuthorize("hasRole('ADMIN')") // Somente usuários com ROLE_ADMIN podem acessar esse endpoint
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10) Pageable page){
        Page<UserResponseDTO> users = userService.findAll(page);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDetailDTO> findById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDetailDTO> findById(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByMe());
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @Valid 
                                                  @RequestBody UserUpdateRequestDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id,dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}