package com.example.demoItmo.service;

import com.example.demoItmo.model.dto.request.CarInfoRequest;
import com.example.demoItmo.model.dto.response.CarInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    public CarInfoResponse createCar(@Valid CarInfoRequest request) {
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

    public CarInfoResponse getCar(Long id) {
        return null;
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
