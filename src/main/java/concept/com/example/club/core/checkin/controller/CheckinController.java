package concept.com.example.club.core.checkin.controller;

import concept.com.example.club.core.checkin.dto.CheckinResponseDTO;
import concept.com.example.club.core.checkin.service.CheckinService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkins")
@AllArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @PreAuthorize("hasRole('BARMAN')")
    @GetMapping("meu-salao")
    public ResponseEntity<Page<CheckinResponseDTO>> findMyCheckins(
            @PageableDefault(size = 10, page = 0, sort = "timestamp") Pageable pageable
    ){
        Page<CheckinResponseDTO> checkins = checkinService.findMyCheckins(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(checkins);
    }



}
