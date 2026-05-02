package concept.com.example.club.controller.hardware;

import concept.com.example.club.service.DeduplicacaoService;
import concept.com.example.club.service.integration.EvolutionApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/totem/checkin")
public class AuthorizationCardNFCController {
    private final EvolutionApiService evolutionApiService;
    private final Logger log = LoggerFactory.getLogger(AuthorizationCardNFCController.class);
    private final DeduplicacaoService deduplicacaoService;

    public AuthorizationCardNFCController(EvolutionApiService evolutionApiService, DeduplicacaoService deduplicacaoService) {
        this.evolutionApiService = evolutionApiService;
        this.deduplicacaoService = deduplicacaoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> authorizationByCard(@PathVariable String id){
        if (deduplicacaoService.isDuplicada(id)){
            log.info("Requisição bloqueada por duplicidade (Anti-Spam).");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Requisição bloqueada por duplicidade (Anti-Spam).");
        }
        log.warn("Mensagem sendo enviada: {}", id);

        //evolutionApiService.sendTextWhatsapp(id);
        evolutionApiService.sendImageWhatsapp(id, "https://raw.githubusercontent.com/Silasmelo12/imagemAlan/refs/heads/main/Screenshot_16.png");


        return ResponseEntity.status(HttpStatus.OK).body("Liberado");
    }
}
