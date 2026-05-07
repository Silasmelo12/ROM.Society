package concept.com.example.club.core.user.controller;

import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.core.user.service.UserService;
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
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // profissionalmente, findAll() deve ter paginação
    @PreAuthorize("hasRole('ADMIN')") // Somente usuários com ROLE_ADMIN podem acessar esse endpoint
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @PageableDefault(page = 0, size = 10) Pageable page){
        Page<UserResponseDTO> users = userService.findAll(page);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String id, @Valid 
                                                  @RequestBody UserUpdateRequestDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id,dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
