package concept.com.example.club.core.user.service;


import concept.com.example.club.common.exception.EmailAlreadyExistsException;
import concept.com.example.club.common.exception.PlanNotAllowedException;
import concept.com.example.club.common.exception.SalonNotFoundException;
import concept.com.example.club.core.salon.model.Salon;
import concept.com.example.club.core.salon.repository.SalonRepository;
import concept.com.example.club.core.user.dto.UserCreateRequestDTO;
import concept.com.example.club.core.user.dto.UserUpdateRequestDTO;
import concept.com.example.club.core.user.dto.UserResponseDTO;
import concept.com.example.club.core.user.dto.UserResponseDetailDTO;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.user.enumeration.Plan;
import concept.com.example.club.core.user.mapper.UserMapper;
import concept.com.example.club.core.user.model.Hobby;
import concept.com.example.club.core.user.model.Preference;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.user.repository.HobbyRepository;
import concept.com.example.club.core.user.repository.PreferenceRepository;
import concept.com.example.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HobbyRepository hobbyRepository;
    private final PreferenceRepository preferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final SalonRepository salonRepository;

    //adicionar variavel de log
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private static final Set<Plan> PLANOS_CONCIERGE = Set.of(Plan.GOLD, Plan.BLACK, Plan.INFINITE);
    private static final Set<Plan> PLANOS_ADMIN = Set.of(Plan.CONCIERGE, Plan.BARMAN);
    private static final Set<Plan> PLANOS_SUPERADMIN = Set.of(Plan.ADMIN);



    public UserResponseDTO create(UserCreateRequestDTO dto, Authentication authentication){

        validarPlanoPorRole(dto.getPlan(), authentication);

        User user = userMapper.toUser(dto);
        user.setActive(true);

        Salon salon = salonRepository.findById(dto.getHomeSalonId()).orElseThrow(
                () -> new SalonNotFoundException("Salão não encontrado.")
        );

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
        user.setHomeSalon(salon);
        user.setPassword(encryptedPassword);
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado.");
        }
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    private void validarPlanoPorRole(Plan planoSolicitado, Authentication authentication) {
        boolean isConcierge = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CONCIERGE"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"));

        if (isConcierge && !PLANOS_CONCIERGE.contains(planoSolicitado)) {
            throw new PlanNotAllowedException(
                    "CONCIERGE só pode criar usuários com plano GOLD, BLACK ou INFINITE."
            );
        }
        if (isAdmin && !PLANOS_ADMIN.contains(planoSolicitado)) {
            throw new PlanNotAllowedException(
                    "ADMIN só pode criar usuários com plano CONCIERGE ou BARMAN."
            );
        }
        if (isSuperAdmin && !PLANOS_SUPERADMIN.contains(planoSolicitado)) {
            throw new PlanNotAllowedException(
                    "SUPERADMIN só pode criar usuários com plano ADMIN."
            );
        }
    }

    public UserResponseDetailDTO findById(String id){

        return userMapper.toUserResponseDetailDTO(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User com userId: "+id+" não foi encontrado.")
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
                .orElseThrow(() -> new UserNotFoundException("User com userId: "+id+" não foi encontrado."));

//        if(!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())){
//            throw new RuntimeException("Email já cadastrado para outro usuário.");
//        }

        userMapper.updateEntityFromDto(dto,user);
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public void delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Usuário não encontrado com o userId: "+id));
        user.setActive(false);
        userRepository.save(user);
    }

    public UserResponseDetailDTO findByMe() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLogado = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return userMapper.toUserResponseDetailDTO(userLogado);
    }
}