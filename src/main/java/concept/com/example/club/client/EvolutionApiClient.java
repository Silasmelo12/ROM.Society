package concept.com.example.club.client;

import concept.com.example.club.config.EvolutionClientConfig;
import concept.com.example.club.dto.integration.EvolutionMediaRequestDTO;
import concept.com.example.club.dto.integration.EvolutionMessageRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "evolution-api", url = "${evolution.api.url}", configuration = EvolutionClientConfig.class)
public interface EvolutionApiClient  {

    @PostMapping("/message/sendText/{instanceName}")
    ResponseEntity<Object> sendTextMessage(
            @PathVariable("instanceName") String instanceName,
            @RequestBody EvolutionMessageRequestDTO MessageRequest
    );

    @PostMapping(value = "/message/sendMedia/{instanceName}", consumes = "application/json")
    ResponseEntity<Object> sendMediaMessage(
            @PathVariable("instanceName") String instance,
            @RequestBody EvolutionMediaRequestDTO request
    );

}
