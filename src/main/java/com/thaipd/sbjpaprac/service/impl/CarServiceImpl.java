package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.entity.CarEntity;
import com.thaipd.sbjpaprac.mapper.MyMapStructMapper;
import com.thaipd.sbjpaprac.repository.CarRepository;
import com.thaipd.sbjpaprac.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final MyMapStructMapper mapper;

    @Override
    public CarDTO create(CarDTO carDTO) {
        CarEntity entity = mapper.toEntity(carDTO);
        CarEntity saved = carRepository.save(entity);
        return mapper.carEntityToCarDTO(saved);
    }

    @Override
    public CarDTO getById(Long id) {
        return carRepository.findById(id)
                .map(mapper::carEntityToCarDTO)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public List<CarDTO> getAll() {
        return carRepository.findAll().stream()
                .map(mapper::carEntityToCarDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarDTO update(Long id, CarDTO carDTO) {
        CarEntity existing = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        existing.setColor(carDTO.getColor());
        existing.setAmountOfSeats(carDTO.getAmountOfSeats());
        existing.setMaxSpeed(carDTO.getMaxSpeed());

        CarEntity updated = carRepository.save(existing);
        return mapper.carEntityToCarDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
    }
}
