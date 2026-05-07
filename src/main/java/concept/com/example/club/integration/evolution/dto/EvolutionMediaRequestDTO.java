package concept.com.example.club.integration.evolution.dto;

public record EvolutionMediaRequestDTO(
        String number,
        String mediatype,
        String mimetype,
        String caption,
        String media,
        String fileName,
        Integer delay
) {
}
