package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.entity.Car;
import com.thaipd.sbjpaprac.mapper.CarMapper;
import com.thaipd.sbjpaprac.repository.CarRepository;
import com.thaipd.sbjpaprac.service.CarService;
import lombok.RequiredArgsConstructor;
import com.thaipd.sbjpaprac.dto.CarSearchCriteria;
import com.thaipd.sbjpaprac.repository.specification.CarSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public List<CarDTO> getAllCars() {
        log.debug("Fetching all cars");
        List<Car> cars = carRepository.findAll();
        return carMapper.toDTOs(cars);
    }

    @Override
    public CarDTO getCarById(Long id) {
        log.debug("Fetching car by id: {}", id);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Car not found with id: {}", id);
                    return new RuntimeException("Car not found with id: " + id);
                });
        return carMapper.toDTO(car);
    }

    @Override
    public CarDTO createCar(CarDTO carDTO) {
        log.info("Creating car: {}", carDTO.getRegistrationNumber());
        Car car = carMapper.toEntity(carDTO);
        Car savedCar = carRepository.save(car);
        log.info("Car created with id: {}", savedCar.getId());
        return carMapper.toDTO(savedCar);
    }

    @Override
    public CarDTO updateCar(Long id, CarDTO carDTO) {
        log.info("Updating car with id: {}", id);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Car not found with id: {}", id);
                    return new RuntimeException("Car not found with id: " + id);
                });

        carMapper.updateEntityFromDTO(carDTO, car);
        Car updatedCar = carRepository.save(car);
        log.info("Car updated with id: {}", updatedCar.getId());
        return carMapper.toDTO(updatedCar);
    }

    @Override
    public void deleteCar(Long id) {
        log.warn("Deleting car with id: {}", id);
        if (!carRepository.existsById(id)) {
            log.error("Car not found with id: {}", id);
            throw new RuntimeException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
        log.info("Car deleted with id: {}", id);
    }

    @Override
    public List<CarDTO> getCarsByBrand(String brand) {
        List<Car> cars = carRepository.findByBrand(brand);
        return carMapper.toDTOs(cars);
    }

    @Override
    public List<CarDTO> getCarsByColor(String color) {
        List<Car> cars = carRepository.findByColor(color);
        return carMapper.toDTOs(cars);
    }

    @Override
    public List<CarDTO> getCarsByModelYear(int modelYear) {
        List<Car> cars = carRepository.findByModelYear(modelYear);
        return carMapper.toDTOs(cars);
    }

    @Override
    public List<CarDTO> getCarsByBrandSortedByYear(String brand) {
        List<Car> cars = carRepository.findByBrandOrderByModelYearAsc(brand);
        return carMapper.toDTOs(cars);
    }

    @Override
    public Page<CarDTO> searchCars(CarSearchCriteria criteria, Pageable pageable) {
        Specification<Car> spec = CarSpecification.getSpec(criteria);
        Page<Car> carPage = carRepository.findAll(spec, pageable);
        return carPage.map(carMapper::toDTO);
    }

    @Override
    public List<CarDTO> getCarsByOwner(Long ownerId) {
        List<Car> cars = carRepository.findByOwner_Ownerid(ownerId);
        return carMapper.toDTOs(cars);
    }
}
