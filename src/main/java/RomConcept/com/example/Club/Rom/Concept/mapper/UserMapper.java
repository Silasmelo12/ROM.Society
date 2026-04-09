package RomConcept.com.example.Club.Rom.Concept.mapper;

import RomConcept.com.example.Club.Rom.Concept.dto.request.UserCreateRequestDTO;
import RomConcept.com.example.Club.Rom.Concept.dto.request.UserUpdateRequestDTO;
import RomConcept.com.example.Club.Rom.Concept.dto.response.UserResponseDTO;
import RomConcept.com.example.Club.Rom.Concept.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target ="id",ignore = true)
    UserCreateRequestDTO toUserCreateRequestDTO(User user);

    User toUser(UserCreateRequestDTO dto);

    User toUser(UserUpdateRequestDTO dto);

    UserResponseDTO toUserResponseDTO(User user);

    List<UserResponseDTO> toUserResponseDTO(List<User> users);
}
