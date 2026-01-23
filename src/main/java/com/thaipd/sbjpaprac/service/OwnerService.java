package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.OwnerDTO;
import java.util.List;

public interface OwnerService {
    List<OwnerDTO> getAllOwners();

    OwnerDTO getOwnerById(Long id);

    OwnerDTO createOwner(OwnerDTO ownerDTO);

    void deleteOwner(Long id);
}
