package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.CarEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CarRepository {

    private final Map<Long, CarEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CarEntity save(CarEntity car) {
        if (car.getId() == null) {
            car.setId(idGenerator.getAndIncrement());
        }
        storage.put(car.getId(), car);
        return car;
    }

    public Optional<CarEntity> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<CarEntity> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
