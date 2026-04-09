package RomConcept.com.example.Club.Rom.Concept.service;


import RomConcept.com.example.Club.Rom.Concept.dto.request.UserCreateRequestDTO;
import RomConcept.com.example.Club.Rom.Concept.dto.request.UserUpdateRequestDTO;
import RomConcept.com.example.Club.Rom.Concept.dto.response.UserResponseDTO;
import RomConcept.com.example.Club.Rom.Concept.exception.UserNotFoundException;
import RomConcept.com.example.Club.Rom.Concept.mapper.UserMapper;
import RomConcept.com.example.Club.Rom.Concept.model.User;
import RomConcept.com.example.Club.Rom.Concept.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserCreateRequestDTO create(UserCreateRequestDTO dto){
        User user = userMapper.toUser(dto);
        return userMapper.toUserCreateRequestDTO(userRepository.save(user));
    }

    public UserResponseDTO findById(Integer id){

        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User com id: {} não foi encontrado."+id)
        ));
    }

    public List<UserResponseDTO> findAll(){
        return userMapper.toUserResponseDTO(userRepository.findAll());
    }

    public UserResponseDTO update(UserUpdateRequestDTO dto){
        User user = userMapper.toUser(dto);
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public void delete(Integer id){
        userRepository.deleteById(id);
    }
}
