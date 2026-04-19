package concept.com.example.club.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvolutionMessageRequestDTO {

    @JsonProperty("number")
    private String number;
    @JsonProperty("text")
    private String text;
    @JsonProperty("options")
    private EvolutionMessageOptionsDTO options;

    public EvolutionMessageRequestDTO(String number, String text, EvolutionMessageOptionsDTO options){
        this.number = number;
        this.text = text;
        this.options = options;
    }
}
