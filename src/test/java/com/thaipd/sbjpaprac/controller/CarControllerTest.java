package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

        @Mock
        private CarService carService;

        @InjectMocks
        private CarController carController;

        @Test
        public void testCreateCar() {
                CarDTO input = CarDTO.builder().color("Red").amountOfSeats(4).maxSpeed(200).build();
                CarDTO saved = CarDTO.builder().id(1L).color("Red").amountOfSeats(4).maxSpeed(200).build();

                when(carService.create(any(CarDTO.class))).thenReturn(saved);

                ResponseEntity<CarDTO> response = carController.create(input);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getId()).isEqualTo(1L);
        }

        @Test
        public void testGetCarById() {
                CarDTO stored = CarDTO.builder().id(1L).color("Blue").build();
                when(carService.getById(1L)).thenReturn(stored);

                ResponseEntity<CarDTO> response = carController.getById(1L);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isEqualTo(stored);
        }

        @Test
        public void testGetAllCars() {
                CarDTO stored = CarDTO.builder().id(1L).color("Blue").build();
                when(carService.getAll()).thenReturn(Collections.singletonList(stored));

                ResponseEntity<List<CarDTO>> response = carController.getAll();

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
        }
}
