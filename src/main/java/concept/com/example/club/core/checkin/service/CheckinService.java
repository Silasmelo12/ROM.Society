package concept.com.example.club.core.checkin.service;

import concept.com.example.club.common.exception.SalonNotFoundException;
import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.common.security.SecurityFilter;
import concept.com.example.club.core.checkin.dto.CheckinResponseDTO;
import concept.com.example.club.core.checkin.enumeration.StatusCheckin;
import concept.com.example.club.core.checkin.mapper.CheckinMapper;
import concept.com.example.club.core.checkin.model.Checkin;
import concept.com.example.club.core.checkin.repository.CheckinRepository;
import concept.com.example.club.core.salon.model.Salon;
import concept.com.example.club.core.salon.repository.SalonRepository;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final UserRepository userRepository;
    private final CheckinMapper checkinMapper;
    private final SalonRepository salonRepository;

    // create checkin
    public CheckinResponseDTO create(String userId, String totemId){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("Usuário não encontrado.")
        );

        Salon salon = salonRepository.findByTotemIdentifier(totemId).orElseThrow(
                () -> new SalonNotFoundException("Totem Não Encontrado.")
        );

        Checkin checkin = new Checkin();
        checkin.setUser(user);
        checkin.setSalon(salon);
        checkin.setStatus(StatusCheckin.ARRIVED);
        Checkin save = checkinRepository.save(checkin);

        return new CheckinResponseDTO(
                save.getId(),
                save.getUser().getId(),
                save.getStatus().toString(),
                save.getSalon().getId()
        );
    }

    public Page<CheckinResponseDTO> findMyCheckins(Pageable pageable){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User barman = userRepository.findByEmail(email).orElseThrow(
                ()-> new UserNotFoundException("Usuário não encontrado.")
        );

        if (barman.getHomeSalon() == null){
            throw new SalonNotFoundException("O usuário não tem um salão associado.");
        }

        return checkinRepository.findBySalonId(barman.getHomeSalon().getId(),pageable)
                .map(checkinMapper::toCheckinResponseDTO);
    }
}
