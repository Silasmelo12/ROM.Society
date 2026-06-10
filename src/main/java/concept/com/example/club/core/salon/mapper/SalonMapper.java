package concept.com.example.club.core.salon.mapper;

import concept.com.example.club.core.salon.dto.SalonCreateRequestDTO;
import concept.com.example.club.core.salon.dto.SalonResponseDTO;
import concept.com.example.club.core.salon.dto.SalonUpdateRequestDTO;
import concept.com.example.club.core.salon.model.Salon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalonMapper {

    @Mapping(target = "id", ignore = true)
    Salon toSalon(SalonCreateRequestDTO dto);

    SalonResponseDTO toSalonResponseDTO(Salon salon);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SalonUpdateRequestDTO dto, @MappingTarget Salon salon);

    List<SalonResponseDTO> toSalonResponseDTO(List<Salon> salons);
}
