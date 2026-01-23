package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.dto.OwnerDTO;
import com.thaipd.sbjpaprac.service.CarService;
import com.thaipd.sbjpaprac.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final CarService carService;

    @GetMapping
    public List<OwnerDTO> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @PostMapping
    public OwnerDTO createOwner(@RequestBody OwnerDTO ownerDTO) {
        return ownerService.createOwner(ownerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cars")
    public List<CarDTO> getCarsByOwner(@PathVariable Long id) {
        return carService.getCarsByOwner(id);
    }
}
