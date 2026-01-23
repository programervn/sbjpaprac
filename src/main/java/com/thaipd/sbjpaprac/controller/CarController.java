package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thaipd.sbjpaprac.dto.CarSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        CarDTO createdCar = carService.createCar(carDTO);
        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        CarDTO carDTO = carService.getCarById(id);
        return ResponseEntity.ok(carDTO);
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        CarDTO updatedCar = carService.updateCar(id, carDTO);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<CarDTO>> getCarsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(carService.getCarsByBrand(brand));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<CarDTO>> getCarsByColor(@PathVariable String color) {
        return ResponseEntity.ok(carService.getCarsByColor(color));
    }

    @GetMapping("/year/{modelYear}")
    public ResponseEntity<List<CarDTO>> getCarsByModelYear(@PathVariable int modelYear) {
        return ResponseEntity.ok(carService.getCarsByModelYear(modelYear));
    }

    @GetMapping("/brand/{brand}/sorted-by-year")
    public ResponseEntity<List<CarDTO>> getCarsByBrandSortedByYear(@PathVariable String brand) {
        return ResponseEntity.ok(carService.getCarsByBrandSortedByYear(brand));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CarDTO>> searchCars(
            CarSearchCriteria criteria,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<CarDTO> cars = carService.searchCars(criteria, pageable);
        return ResponseEntity.ok(cars);
    }
}
