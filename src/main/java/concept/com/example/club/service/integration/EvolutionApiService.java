package concept.com.example.club.service.integration;

import concept.com.example.club.client.EvolutionApiClient;
import concept.com.example.club.dto.integration.EvolutionMessageOptionsDTO;
import concept.com.example.club.dto.integration.EvolutionMessageRequestDTO;
import concept.com.example.club.dto.response.UserResponseDTO;
import concept.com.example.club.service.UserService;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EvolutionApiService {
    private final EvolutionApiClient evolutionApiClient;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(EvolutionApiService.class);


    public EvolutionApiService(EvolutionApiClient evolutionApiClient, UserService userService) {
        this.evolutionApiClient = evolutionApiClient;
        this.userService = userService;
    }


    public UserResponseDTO sendTextWhatsapp(String id){
        UserResponseDTO user = userService.findById(id);
        EvolutionMessageRequestDTO evolutionMessageRequestDTO = getEvolutionMessageRequestDTO(user);
        try {
            ResponseEntity<Object> response = evolutionApiClient.sendTextMessage("Tetrix_Bot", evolutionMessageRequestDTO);
            log.info("Mensagem enviada com sucesso. Status: {}. Resposta: {}", response.getStatusCode(), response.getBody());
        }catch(Exception e){
            throw  new RuntimeException("Falha na comunicação com evolution api "+e.getMessage());
        }
        return user;
    }

    private static @NonNull EvolutionMessageRequestDTO getEvolutionMessageRequestDTO(UserResponseDTO user) {
        String phone = user.getPhone();
        String text = "\uD83D\uDC8E ROM Society | "+user.getPlan()+" \uD83D\uDC8E\n\n" +
                "Nosso cliente exclusivo,  *Sr. "+user.getName()+"*, acaba de chegar.\n\n" +
                "☕ Ação Imediata:\n" +
                "Por favor, antecipem o conforto dele preparando um café espresso sem açúcar.\n\n" +
                "\uD83C\uDF77 Toque de Excelência:\n" +
                "O Sr. "+user.getName()+" é um apreciador de *vinhos* de Bordeaux. " +
                "Usem essa informação para criar conexão e oferecer uma recepção agradável.\n";
        EvolutionMessageOptionsDTO optionsDTO = new EvolutionMessageOptionsDTO(
                1200,
                "composing",
                false
        );
        return new EvolutionMessageRequestDTO(
                phone,
                text,
                optionsDTO
        );
    }
}
