package com.coding.car_rental_app.services.customer;

import com.coding.car_rental_app.dtos.BookingDetailsDTO;
import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.dtos.CarDTOPage;
import com.coding.car_rental_app.entity.Car;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    List<CarDTO> findAllCars();

    CarDTOPage getAllCars(int page, int size);

    CarDTO getCarById(Long carId);

    boolean bookCar(BookingDetailsDTO bookingDetailsDTO);
}
