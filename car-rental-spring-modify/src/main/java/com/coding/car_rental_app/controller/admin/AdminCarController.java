package com.coding.car_rental_app.controller.admin;

import com.coding.car_rental_app.apiresponse.ApiResponse;
import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.services.admin.AdminService;
import com.coding.car_rental_app.validation.dtogroupvalidator.OnCreate;
import com.coding.car_rental_app.validation.dtogroupvalidator.OnUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminCarController {

    private final AdminService adminService;

    @PostMapping("/add-car")
    public ResponseEntity<ApiResponse<CarDTO>> addCar(@ModelAttribute @Validated(OnCreate.class) CarDTO carDTO) throws IOException {
           CarDTO dbCarDTO = adminService.addCar(carDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Car Added Success!", dbCarDTO));
    }

    @GetMapping("/cars")
    public ResponseEntity<ApiResponse<List<CarDTO>>> getAllCars() {
        List<CarDTO> cars = adminService.getAllCars();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>("Success", cars));
    }

    @DeleteMapping("/car/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
               adminService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<ApiResponse<CarDTO>> getCarById(@PathVariable Long carId) {
        CarDTO carDTO = adminService.findCarById(carId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>("Success", carDTO));
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<ApiResponse<CarDTO>> updateCar(@PathVariable Long carId,
                                            @ModelAttribute @Validated(OnUpdate.class) CarDTO carDTO) {
        CarDTO carDTO1 = adminService.updateCar(carId, carDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>("Car Updated Success", carDTO1));

    }
}
