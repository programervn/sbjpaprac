package com.thaipd.sbjpaprac.mapper;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = { OwnerMapper.class })
public interface CarMapper {
    CarDTO toDTO(Car car);

    Car toEntity(CarDTO carDTO);

    List<CarDTO> toDTOs(List<Car> cars);

    void updateEntityFromDTO(CarDTO carDTO, @MappingTarget Car car);
}
