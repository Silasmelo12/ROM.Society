package concept.com.example.club.core.checkin.mapper;

import concept.com.example.club.core.checkin.dto.CheckinResponseDTO;
import concept.com.example.club.core.checkin.model.Checkin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckinMapper {

    CheckinResponseDTO toCheckinResponseDTO(Checkin checkin);

}
