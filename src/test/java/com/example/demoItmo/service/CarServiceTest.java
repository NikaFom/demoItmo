package com.example.demoItmo.service;

import com.example.demoItmo.model.db.entity.CarEntity;
import com.example.demoItmo.model.db.entity.UserEntity;
import com.example.demoItmo.model.db.repository.CarRepository;
import com.example.demoItmo.model.dto.request.CarInfoRequest;
import com.example.demoItmo.model.dto.request.CarToUserRequest;
import com.example.demoItmo.model.dto.response.CarInfoResponse;
import com.example.demoItmo.model.enums.CarStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private UserService userService;

    @Mock
    private CarRepository carRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createCar() {
        CarInfoRequest request = new CarInfoRequest();

        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.save(any(CarEntity.class))).thenReturn(car);

        CarInfoResponse result = carService.createCar(request);

        assertEquals(car.getId(), result.getId());
    }

    @Test
    public void getCar() {
        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        CarInfoResponse result = carService.getCar(car.getId());

        assertEquals(car.getId(), result.getId());
    }

    @Test
    public void updateCar() {
        CarInfoRequest request = new CarInfoRequest();

        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        carService.updateCar(car.getId(), request);

        verify(carRepository, times(1)).save(any(CarEntity.class));
        assertEquals(CarStatus.UPDATED, car.getStatus());
    }

    @Test
    public void updateCar_missingFields() {
        CarInfoRequest request = new CarInfoRequest();
        request.setModel("");
        request.setYear(0);
        request.setDoors(0);
        request.setCapacity(0.0);
        request.setSpeed(0.0);

        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        carService.updateCar(car.getId(), request);

        verify(carRepository, times(1)).save(any(CarEntity.class));
        assertEquals(CarStatus.UPDATED, car.getStatus());
    }

    @Test
    public void deleteCar() {
        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        carService.deleteCar(car.getId());

        verify(carRepository, times(1)).save(any(CarEntity.class));
        assertEquals(CarStatus.DELETED, car.getStatus());
    }

    @Test
    public void getAllCars_notFiltered() {
        CarEntity car1 = new CarEntity();
        car1.setId(1L);
        car1.setBrand("BMW");
        CarEntity car2 = new CarEntity();
        car2.setId(2L);

        List<CarEntity> cars = List.of(car1, car2);

        Page<CarEntity> pagedCars = new PageImpl<>(cars);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, car1.getBrand());

        when(carRepository.findAllByStatusNot(pageRequest, CarStatus.DELETED)).thenReturn(pagedCars);

        Page <CarInfoResponse> result = carService
                .getAllCars(0, 10, car1.getBrand(), Sort.Direction.ASC, null);

        assertEquals(pagedCars.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllCars_Filtered() {
        CarEntity car1 = new CarEntity();
        car1.setId(1L);
        car1.setBrand("BMW");
        CarEntity car2 = new CarEntity();
        car2.setId(2L);

        List<CarEntity> cars = List.of(car1, car2);

        Page<CarEntity> pagedCars = new PageImpl<>(cars);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, car1.getBrand());

        when(carRepository.findAllByStatusNotFiltered(pageRequest, CarStatus.DELETED, "a"))
                .thenReturn(pagedCars);

        Page <CarInfoResponse> result = carService
                .getAllCars(0, 10, car1.getBrand(), Sort.Direction.ASC, "a");

        assertEquals(pagedCars.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addCarToUser() {
        CarEntity car = new CarEntity();
        car.setId(1L);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userService.getUserFromDB(user.getId())).thenReturn(user);

        user.setCars(new ArrayList<>());
        when(userService.updateUserData(any(UserEntity.class))).thenReturn(user);

        CarToUserRequest request = new CarToUserRequest();
        request.setCarId(car.getId());
        request.setUserId(user.getId());

        carService.addCarToUser(request);

        verify(carRepository, times(1)).save(any(CarEntity.class));
        assertEquals(user.getId(), car.getUser().getId());
    }

    @Test
    public void getUserCars() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userService.getUserFromDB(user.getId())).thenReturn(user);

        List<CarEntity> userCars = List.of(new CarEntity(), new CarEntity());
        user.setCars(userCars);

        List<CarInfoResponse> result = carService.getUserCars(user.getId());

        assertEquals(userCars.size(), result.size());
    }
}