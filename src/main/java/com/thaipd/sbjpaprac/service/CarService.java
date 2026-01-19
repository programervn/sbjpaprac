package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.CarDTO;

import java.util.List;

public interface CarService {

    CarDTO create(CarDTO carDTO);

    CarDTO getById(Long id);

    List<CarDTO> getAll();

    CarDTO update(Long id, CarDTO carDTO);

    void delete(Long id);
}
