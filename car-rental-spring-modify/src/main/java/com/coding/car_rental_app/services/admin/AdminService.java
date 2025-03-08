package com.coding.car_rental_app.services.admin;

import com.coding.car_rental_app.dtos.CarDTO;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    CarDTO addCar(CarDTO carDTO) throws IOException;

    List<CarDTO> getAllCars();

    void deleteCar(Long carId);

    CarDTO findCarById(Long carId);

    CarDTO updateCar(Long carId, CarDTO carDTO);
}
