package com.example.demoItmo.controllers;

import com.example.demoItmo.model.dto.request.CarInfoRequest;
import com.example.demoItmo.model.dto.response.CarInfoResponse;
import com.example.demoItmo.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Автомобили")
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Создать автомобиль")
    public CarInfoResponse createCar(@RequestBody @Valid CarInfoRequest request) {
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить автомобиль по ID")
    public CarInfoResponse getCar(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить автомобиль по ID")
    public CarInfoResponse updateCar(@PathVariable Long id, @RequestBody @Valid CarInfoRequest request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить автомобиль по ID")
    public void deleteCar (@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список автомобилей")
    public List<CarInfoResponse> getAllCars() {
        return carService.getAllCars();
    }
}
