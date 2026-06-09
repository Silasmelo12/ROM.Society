package concept.com.example.club.core.hardware.controller;

import concept.com.example.club.core.checkin.dto.CheckinResponseDTO;
import concept.com.example.club.core.checkin.service.CheckinService;
import concept.com.example.club.core.hardware.dto.TotemCheckinRequestDTO;
import concept.com.example.club.core.hardware.service.DeduplicacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/totem/checkin")
public class AuthorizationCardNFCController {
    private final Logger log = LoggerFactory.getLogger(AuthorizationCardNFCController.class);
    private final DeduplicacaoService deduplicacaoService;
    // Use uma constante ou traga do application.properties com @Value("${totem.api.key}")
    @Value("${totem.api.key}")
    private String TOTEM_SECRET_KEY;// = "ROM-CONCEPT-TOTEM-SECURE-KEY-2026";
    private final CheckinService checkinService;

    public AuthorizationCardNFCController(DeduplicacaoService deduplicacaoService, CheckinService checkinService) {
        //this.evolutionApiService = evolutionApiService;
        this.deduplicacaoService = deduplicacaoService;
        this.checkinService = checkinService;
    }

    @PostMapping
    public ResponseEntity<CheckinResponseDTO > authorizationByCard(@RequestBody TotemCheckinRequestDTO dto,
                                                      @RequestHeader(value = "X-Totem-Key", required = false) String apiKey,
                                                      @RequestHeader(value = "X-Totem-Id") String totemId){

        // 1. A Trava de Segurança M2M
        if (apiKey == null || !apiKey.equals(TOTEM_SECRET_KEY)) {
            log.warn("Tentativa de acesso não autorizada na rota do Totem. IP não confiável.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (deduplicacaoService.isDuplicada(dto.userId())){
            log.info("Requisição bloqueada por duplicidade (Anti-Spam).");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }
        log.warn("Mensagem sendo enviada: {}", dto.userId());
        CheckinResponseDTO checkinResponseDTO = checkinService.create(dto.userId(),totemId);
        //evolutionApiService.sendImageWhatsapp(userId, "https://raw.githubusercontent.com/Silasmelo12/imagemAlan/refs/heads/main/Screenshot_16.png");

        return ResponseEntity.status(HttpStatus.OK).body(checkinResponseDTO);
    }
}
