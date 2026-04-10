package concept.com.example.club.service;


import concept.com.example.club.dto.request.UserCreateRequestDTO;
import concept.com.example.club.dto.request.UserUpdateRequestDTO;
import concept.com.example.club.dto.response.UserResponseDTO;
import concept.com.example.club.exception.UserNotFoundException;
import concept.com.example.club.mapper.UserMapper;
import concept.com.example.club.model.User;
import concept.com.example.club.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO create(UserCreateRequestDTO dto){
        User user = userMapper.toUser(dto);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO findById(String id){

        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User com id: {} não foi encontrado."+id)
        ));
    }

    public List<UserResponseDTO> findAll(){
        return userMapper.toUserResponseDTO(userRepository.findAll());
    }


    public UserResponseDTO update(String id, UserUpdateRequestDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User com id: {} não foi encontrado." + id));
        userMapper.toUser(dto,user);
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public void delete(String id){
        userRepository.deleteById(id);
    }

    public void softDelete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Usuário não encontrado com o id: {}"+id));
        user.setActive(false);
        userRepository.save(user);
    }

}
