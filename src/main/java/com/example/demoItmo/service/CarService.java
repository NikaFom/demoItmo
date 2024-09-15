package com.example.demoItmo.service;

import com.example.demoItmo.model.db.entity.CarEntity;
import com.example.demoItmo.model.db.repository.CarRepository;
import com.example.demoItmo.model.dto.request.CarInfoRequest;
import com.example.demoItmo.model.dto.response.CarInfoResponse;
import com.example.demoItmo.model.enums.CarStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    private final ObjectMapper mapper;
    private final CarRepository carRepository;

    public CarInfoResponse createCar(@Valid CarInfoRequest request) {
        CarEntity car = mapper.convertValue(request, CarEntity.class);
        car.setCreatedAt(LocalDateTime.now());
        car.setStatus(CarStatus.CREATED);

        CarEntity savedCar = carRepository.save(car);

        return mapper.convertValue(savedCar, CarInfoResponse.class);
    }

    public CarInfoResponse getCar(Long id) {
        CarEntity car = getCarFromDB(id);
        return mapper.convertValue(car, CarInfoResponse.class);
    }

    private CarEntity getCarFromDB(Long id) {
        return carRepository.findById(id).orElse(new CarEntity());
    }

    public CarInfoResponse updateCar(Long id, @Valid CarInfoRequest request) {
        CarEntity car = getCarFromDB(id);

        car.setBrand(request.getBrand());
        car.setModel(request.getModel() == null ? car.getModel() : request.getModel());
        car.setYear(request.getYear() == null ? car.getYear() : request.getYear());
        car.setDoors(request.getDoors() == null ? car.getYear() : request.getDoors());
        car.setCapacity(request.getCapacity() == null ? car.getCapacity() : request.getCapacity());
        car.setSpeed(request.getSpeed() == null ? car.getSpeed() : request.getSpeed());
        car.setColour(request.getColour() == null ? car.getColour() : request.getColour());


        car.setUpdatedAt(LocalDateTime.now());
        car.setStatus(CarStatus.UPDATED);

        CarEntity savedCar = carRepository.save(car);

        return mapper.convertValue(savedCar, CarInfoResponse.class);
    }

    public void deleteCar(Long id) {
        CarEntity car = getCarFromDB(id);
        car.setUpdatedAt(LocalDateTime.now());
        car.setStatus(CarStatus.DELETED);
        carRepository.save(car);
    }

    public List<CarInfoResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(carEntity -> mapper.convertValue(carEntity, CarInfoResponse.class))
                .collect(Collectors.toList());
    }
}
