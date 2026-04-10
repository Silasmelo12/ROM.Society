package concept.com.example.club.mapper;

import concept.com.example.club.dto.request.UserCreateRequestDTO;
import concept.com.example.club.dto.request.UserUpdateRequestDTO;
import concept.com.example.club.dto.response.UserResponseDTO;
import concept.com.example.club.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //@Mapping(target ="id",ignore = true)
    UserCreateRequestDTO toUserCreateRequestDTO(User user);

    User toUser(UserCreateRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    void toUser(UserUpdateRequestDTO dto, @MappingTarget User user);

    UserResponseDTO toUserResponseDTO(User user);

    List<UserResponseDTO> toUserResponseDTO(List<User> users);
}
