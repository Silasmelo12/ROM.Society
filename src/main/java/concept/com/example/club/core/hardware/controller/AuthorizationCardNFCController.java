package concept.com.example.club.core.hardware.controller;

import concept.com.example.club.core.checkin.service.CheckinService;
import concept.com.example.club.core.hardware.service.DeduplicacaoService;
import concept.com.example.club.integration.evolution.service.EvolutionApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/totem/checkin")
public class AuthorizationCardNFCController {
    private final EvolutionApiService evolutionApiService;
    private final Logger log = LoggerFactory.getLogger(AuthorizationCardNFCController.class);
    private final DeduplicacaoService deduplicacaoService;
    // Use uma constante ou traga do application.properties com @Value("${totem.api.key}")
    private final String TOTEM_SECRET_KEY = "ROM-CONCEPT-TOTEM-SECURE-KEY-2026";
    private final CheckinService checkinService;

    public AuthorizationCardNFCController(EvolutionApiService evolutionApiService, DeduplicacaoService deduplicacaoService, CheckinService checkinService) {
        this.evolutionApiService = evolutionApiService;
        this.deduplicacaoService = deduplicacaoService;
        this.checkinService = checkinService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> authorizationByCard(@PathVariable String id,
                                                      @RequestHeader(value = "X-Totem-Key", required = false) String apiKey){

        // 1. A Trava de Segurança M2M
        if (apiKey == null || !apiKey.equals(TOTEM_SECRET_KEY)) {
            log.warn("Tentativa de acesso não autorizada na rota do Totem. IP não confiável.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");
        }

        if (deduplicacaoService.isDuplicada(id)){
            log.info("Requisição bloqueada por duplicidade (Anti-Spam).");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Requisição bloqueada por duplicidade (Anti-Spam).");
        }
        log.warn("Mensagem sendo enviada: {}", id);
        checkinService.create(id);
        //evolutionApiService.sendImageWhatsapp(id, "https://raw.githubusercontent.com/Silasmelo12/imagemAlan/refs/heads/main/Screenshot_16.png");

        return ResponseEntity.status(HttpStatus.OK).body("Liberado");
    }
}
