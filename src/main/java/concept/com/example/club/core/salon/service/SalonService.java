package concept.com.example.club.core.salon.service;

import concept.com.example.club.common.exception.SalonNotFoundException;
import concept.com.example.club.core.salon.dto.SalonCreateRequestDTO;
import concept.com.example.club.core.salon.dto.SalonResponseDTO;
import concept.com.example.club.core.salon.dto.SalonUpdateRequestDTO;
import concept.com.example.club.core.salon.mapper.SalonMapper;
import concept.com.example.club.core.salon.model.Salon;
import concept.com.example.club.core.salon.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalonService {

    private static final Logger log = LoggerFactory.getLogger(SalonService.class);

    private final SalonRepository salonRepository;
    private final SalonMapper salonMapper;

    @Transactional
    public SalonResponseDTO create(SalonCreateRequestDTO dto) {
        log.info("Criando novo salão com totemIdentifier: {}", dto.getTotemIdentifier());

        Salon salon = salonMapper.toSalon(dto);
        Salon saved = salonRepository.save(salon);

        log.info("Salão criado com id: {}", saved.getId());
        return salonMapper.toSalonResponseDTO(saved);
    }

    public SalonResponseDTO findById(String id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException("Salão não encontrado com o id: " + id));

        return salonMapper.toSalonResponseDTO(salon);
    }

    public Page<SalonResponseDTO> findAll(Pageable pageable) {
        Page<Salon> salonsPage = salonRepository.findAll(pageable);
        return salonsPage.map(salonMapper::toSalonResponseDTO);
    }

    @Transactional
    public SalonResponseDTO update(String id, SalonUpdateRequestDTO dto) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException("Salão não encontrado com o id: " + id));

        salonMapper.updateEntityFromDto(dto, salon);
        salonRepository.save(salon);

        log.info("Salão atualizado com id: {}", id);
        return salonMapper.toSalonResponseDTO(salon);
    }

    @Transactional
    public void delete(String id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException("Salão não encontrado com o id: " + id));

        salonRepository.delete(salon);
        log.info("Salão deletado com id: {}", id);
    }

    public SalonResponseDTO findByTotemIdentifier(String totemIdentifier) {
        Salon salon = salonRepository.findByTotemIdentifier(totemIdentifier)
                .orElseThrow(() -> new SalonNotFoundException("Salão não encontrado com o totemIdentifier: " + totemIdentifier));

        return salonMapper.toSalonResponseDTO(salon);
    }
}
