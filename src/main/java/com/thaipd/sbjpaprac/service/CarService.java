package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.thaipd.sbjpaprac.dto.CarSearchCriteria;

import java.util.List;

public interface CarService {
    List<CarDTO> getAllCars();

    CarDTO getCarById(Long id);

    CarDTO createCar(CarDTO carDTO);

    CarDTO updateCar(Long id, CarDTO carDTO);

    void deleteCar(Long id);

    List<CarDTO> getCarsByBrand(String brand);

    List<CarDTO> getCarsByColor(String color);

    List<CarDTO> getCarsByModelYear(int modelYear);

    List<CarDTO> getCarsByBrandSortedByYear(String brand);

    Page<CarDTO> searchCars(CarSearchCriteria criteria, Pageable pageable);

    List<CarDTO> getCarsByOwner(Long ownerId);
}
