package com.example.demoItmo.controllers;

import com.example.demoItmo.model.dto.request.CarInfoRequest;
import com.example.demoItmo.model.dto.response.CarInfoResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @PostMapping
    public CarInfoResponse createCar(@RequestBody CarInfoRequest request) {
        return new CarInfoResponse();
    }

    @GetMapping("/{id}")
    public CarInfoResponse getCar(@PathVariable Long id) {
        return new CarInfoResponse();
    }

    @PutMapping("/{id}")
    public CarInfoResponse updateCar(@PathVariable Long id, @RequestBody CarInfoRequest request) {
        return new CarInfoResponse();
    }

    @DeleteMapping("/{id}")
    public void deleteCar (@PathVariable Long id) {

    }

    @GetMapping("/all")
    public List<CarInfoResponse> getAllCars() {
        return Collections.singletonList(new CarInfoResponse());
    }
}
