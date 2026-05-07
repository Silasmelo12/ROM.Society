package concept.com.example.club.core.user.service;


import concept.com.example.club.core.user.dto.UserCreateRequestDTO;
import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.user.mapper.UserMapper;
import concept.com.example.club.core.user.model.Hobby;
import concept.com.example.club.core.user.model.Preference;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.user.repository.HobbyRepository;
import concept.com.example.club.core.user.repository.PreferenceRepository;
import concept.com.example.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HobbyRepository hobbyRepository;
    private final PreferenceRepository preferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO create(UserCreateRequestDTO dto){
        User user = userMapper.toUser(dto);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        List<Hobby> hobbiesExisteds = hobbyRepository.findByNameIn(dto.getHobbies());

        for (Hobby hobby:hobbiesExisteds){
            user.addHobby(hobby);
        }

        List<Preference> preferencesExisteds = preferenceRepository.findByNameIn(dto.getPreferences());
        for(Preference preference:preferencesExisteds){
            user.addPreference(preference);
        }

        List<String> nomesJaCadastrados = hobbiesExisteds
                .stream()
                .map(Hobby::getName)
                .toList();

        List<String> preferencesJaCadastrados = preferencesExisteds
                .stream()
                .map(Preference::getName)
                .toList();

        for (String nomeRecebido : dto.getPreferences()){
            if (!preferencesJaCadastrados.contains(nomeRecebido)){
                Preference newPreference = new Preference(nomeRecebido);
                user.addPreference(newPreference);
            }
        }

        for (String nomeRecebido : dto.getHobbies()){
            if (!nomesJaCadastrados.contains(nomeRecebido)){
                Hobby newHobby = new Hobby(nomeRecebido);
                user.addHobby(newHobby);
            }
        }
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encryptedPassword);
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO findById(String id){

        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User com id: "+id+" não foi encontrado.")
        ));
    }

    public UserResponseDTO findByEmail(String email){
        return userMapper.toUserResponseDTO(userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User com email: "+email+" não foi encontrado.")
        ));
    }

    public Page<UserResponseDTO> findAll(Pageable pageable){
        Page<User> usersPage = userRepository.findByActiveTrue(pageable);
        return usersPage.map(user -> userMapper.toUserResponseDTO(user));
    }

    public UserResponseDTO update(String id, UserUpdateRequestDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User com id: "+id+" não foi encontrado."));

        if(!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já cadastrado para outro usuário.");
        }

        userMapper.updateEntityFromDto(dto,user);
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public void delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Usuário não encontrado com o id: "+id));
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);
    }

}
