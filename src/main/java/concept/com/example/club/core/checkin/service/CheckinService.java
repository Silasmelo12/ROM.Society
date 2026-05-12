package concept.com.example.club.core.checkin.service;

import concept.com.example.club.common.exception.UserNotFoundException;
import concept.com.example.club.core.checkin.dto.CheckinResponseDTO;
import concept.com.example.club.core.checkin.enumeration.StatusCheckin;
import concept.com.example.club.core.checkin.mapper.CheckinMapper;
import concept.com.example.club.core.checkin.model.Checkin;
import concept.com.example.club.core.checkin.repository.CheckinRepository;
import concept.com.example.club.core.user.model.User;
import concept.com.example.club.core.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final UserRepository userRepository;
    private final CheckinMapper checkinMapper;

    // create checkin
    public CheckinResponseDTO create(String id){

        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("Usuário não encontrado.")
        );

        Checkin checkin = new Checkin();
        checkin.setUser(user);
        checkin.setStatus(StatusCheckin.ARRIVED);
        Checkin save = checkinRepository.save(checkin);

        return new CheckinResponseDTO(
                save.getId(),
                save.getUser().getId(),
                save.getStatus().toString()
        );
    }

    public Page<CheckinResponseDTO> findAll(Pageable pageable){
        Page<Checkin> checkinsPage = checkinRepository.findAll(pageable);
        return checkinsPage.map(checkin -> checkinMapper.toCheckinResponseDTO(checkin)
        );
    }
}
