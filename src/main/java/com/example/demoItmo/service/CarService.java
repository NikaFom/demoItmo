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
        CarEntity car = carRepository.findById(id).orElse(new CarEntity());
        return mapper.convertValue(car, CarInfoResponse.class);
    }

    public CarInfoResponse updateCar(Long id, @Valid CarInfoRequest request) {
        return CarInfoResponse.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .doors(request.getDoors())
                .capacity(request.getCapacity())
                .speed(request.getSpeed())
                .colour(request.getColour())
                .build();
    }

    public void deleteCar(Long id) {
    }

    public List<CarInfoResponse> getAllCars() {
        return Collections.emptyList();
    }
}
