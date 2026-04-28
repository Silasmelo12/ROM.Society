package concept.com.example.club.dto.integration;

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
