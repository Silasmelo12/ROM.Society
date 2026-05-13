package concept.com.example.club.core.user.mapper;

import concept.com.example.club.core.user.dto.UserCreateRequestDTO;
import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.core.user.dto.UserResponseDetailDTO;
import concept.com.example.club.core.user.model.Hobby;
import concept.com.example.club.core.user.model.Preference;
import concept.com.example.club.core.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Mapping(target = "hobbies", ignore = true)
    @Mapping(target = "preferences", ignore = true)
    void updateEntityFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);

    UserResponseDTO toUserResponseDTO(User user);
    
    @Mapping(target = "hobbies", source = "hobbies")
    @Mapping(target = "preferences", source = "preferences")
    UserResponseDetailDTO toUserResponseDetailDTO(User user);

    List<UserResponseDTO> toUserResponseDTO(List<User> users);
    
    default Set<String> mapHobbies(Set<Hobby> hobbies) {
        if (hobbies == null) {
            return null;
        }
        return hobbies.stream()
                .map(Hobby::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPreferences(Set<Preference> preferences) {
        if (preferences == null) {
            return null;
        }
        return preferences.stream()
                .map(Preference::getName)
                .collect(Collectors.toSet());
    }
}