package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.OwnerDTO;
import com.thaipd.sbjpaprac.entity.Owner;
import com.thaipd.sbjpaprac.mapper.OwnerMapper;
import com.thaipd.sbjpaprac.repository.OwnerRepository;
import com.thaipd.sbjpaprac.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(ownerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OwnerDTO getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
        return ownerMapper.toDTO(owner);
    }

    @Override
    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toDTO(savedOwner);
    }

    @Override
    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new RuntimeException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }
}
