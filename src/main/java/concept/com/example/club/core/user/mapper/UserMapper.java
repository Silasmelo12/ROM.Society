package concept.com.example.club.core.user.mapper;

import concept.com.example.club.core.user.dto.UserCreateRequestDTO;
import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.core.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    //@Mapping(target ="id",ignore = true)
    @Mapping(target = "hobbies", ignore = true)
    @Mapping(target = "preferences", ignore = true)
    UserCreateRequestDTO toUserCreateRequestDTO(User user);

    @Mapping(target = "hobbies", ignore = true)
    @Mapping(target = "preferences", ignore = true)
    User toUser(UserCreateRequestDTO dto);
    User toUser(UserResponseDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    void updateEntityFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);

    UserResponseDTO toUserResponseDTO(User user);

    List<UserResponseDTO> toUserResponseDTO(List<User> users);
}
