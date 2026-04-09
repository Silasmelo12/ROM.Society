package RomConcept.com.example.Club.Rom.Concept.service;


import RomConcept.com.example.Club.Rom.Concept.dto.request.UserCreateRequestDTO;
import RomConcept.com.example.Club.Rom.Concept.mapper.UserMapper;
import RomConcept.com.example.Club.Rom.Concept.model.User;
import RomConcept.com.example.Club.Rom.Concept.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserCreateRequestDTO create(UserCreateRequestDTO dto){
        User user = userMapper.toUser(dto);
        return userMapper.toUserCreateRequestDTO(userRepository.save(user));
    }



}
