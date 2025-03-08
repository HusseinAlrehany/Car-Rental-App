package com.coding.car_rental_app.controller.admin;

import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.services.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCarController {

    private final AdminService adminService;

    @PostMapping("/add-car")
    public ResponseEntity<?> addCar(@ModelAttribute @Valid CarDTO carDTO) throws IOException {
        CarDTO dbCarDTO = adminService.addCar(carDTO);

        return dbCarDTO != null ? ResponseEntity.ok(dbCarDTO) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Car Not added!"));
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {
        List<CarDTO> cars = adminService.getAllCars();
        return cars.isEmpty() ? ResponseEntity.badRequest().build() :
                ResponseEntity.ok(cars);
    }

    @DeleteMapping("/car/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        adminService.deleteCar(carId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long carId) {
        CarDTO carDTO = adminService.findCarById(carId);
        return carDTO != null ? ResponseEntity.ok(carDTO) :
                ResponseEntity.badRequest().build();
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long carId,
                                            @ModelAttribute @Valid CarDTO carDTO) {
        CarDTO carDTO1 = adminService.updateCar(carId, carDTO);
        return carDTO1 != null ? ResponseEntity.ok(carDTO1) :
                ResponseEntity.badRequest().build();

    }
}
