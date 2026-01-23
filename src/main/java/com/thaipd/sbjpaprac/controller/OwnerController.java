package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.dto.OwnerDTO;
import com.thaipd.sbjpaprac.service.CarService;
import com.thaipd.sbjpaprac.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "Owner Management", description = "Endpoints for managing owners and their cars")
public class OwnerController {

    private final OwnerService ownerService;
    private final CarService carService;

    @GetMapping
    @Operation(summary = "Get all owners")
    public List<OwnerDTO> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @PostMapping
    public OwnerDTO createOwner(@RequestBody OwnerDTO ownerDTO) {
        log.info("REST request to save owner: {} {}", ownerDTO.getFirstname(), ownerDTO.getLastname());
        return ownerService.createOwner(ownerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.info("REST request to delete owner: {}", id);
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cars")
    @Operation(summary = "Get all cars belonging to a specific owner")
    public List<CarDTO> getCarsByOwner(@PathVariable Long id) {
        return carService.getCarsByOwner(id);
    }
}
