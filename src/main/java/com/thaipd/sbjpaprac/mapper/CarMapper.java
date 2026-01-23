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

/*
 * // Cách 2: Không sử dụng 'uses', tự map thủ công hoặc dùng expression
 * // Lưu ý: Cần đổi interface thành abstract class nếu muốn inject OwnerMapper
 * thủ công
 * // hoặc dùng @Mapping(target = "owner", expression = "java(...)")
 * 
 * @Mapper(componentModel = "spring")
 * public abstract class CarMapper {
 * 
 * @Autowired
 * protected OwnerMapper ownerMapper;
 * 
 * @Mapping(target = "owner", expression =
 * "java(ownerMapper.toDTO(car.getOwner()))")
 * public abstract CarDTO toDTO(Car car);
 * 
 * @Mapping(target = "owner", expression =
 * "java(ownerMapper.toEntity(carDTO.getOwner()))")
 * public abstract Car toEntity(CarDTO carDTO);
 * 
 * public abstract List<CarDTO> toDTOs(List<Car> cars);
 * 
 * public abstract void updateEntityFromDTO(CarDTO carDTO, @MappingTarget Car
 * car);
 * }
 */
