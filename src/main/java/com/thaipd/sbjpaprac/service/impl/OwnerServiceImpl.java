package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.OwnerDTO;
import com.thaipd.sbjpaprac.entity.Owner;
import com.thaipd.sbjpaprac.mapper.OwnerMapper;
import com.thaipd.sbjpaprac.repository.OwnerRepository;
import com.thaipd.sbjpaprac.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public List<OwnerDTO> getAllOwners() {
        log.debug("Fetching all owners");
        return ownerRepository.findAll().stream()
                .map(ownerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OwnerDTO getOwnerById(Long id) {
        log.debug("Fetching owner by id: {}", id);
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Owner not found with id: {}", id);
                    return new RuntimeException("Owner not found with id: " + id);
                });
        return ownerMapper.toDTO(owner);
    }

    @Override
    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        log.info("Creating owner: {} {}", ownerDTO.getFirstname(), ownerDTO.getLastname());
        Owner owner = ownerMapper.toEntity(ownerDTO);
        Owner savedOwner = ownerRepository.save(owner);
        log.info("Owner created with id: {}", savedOwner.getOwnerid());
        return ownerMapper.toDTO(savedOwner);
    }

    @Override
    public void deleteOwner(Long id) {
        log.warn("Deleting owner with id: {}", id);
        if (!ownerRepository.existsById(id)) {
            log.error("Owner not found with id: {}", id);
            throw new RuntimeException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
        log.info("Owner deleted with id: {}", id);
    }
}
