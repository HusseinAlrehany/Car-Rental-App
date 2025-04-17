package com.coding.car_rental_app.controller.customer;

import com.coding.car_rental_app.apiresponse.ApiResponse;
import com.coding.car_rental_app.dtos.BookingDetailsDTO;
import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.dtos.CarDTOPage;
import com.coding.car_rental_app.services.customer.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAuthority('CUSTOMER')")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    
    @GetMapping("/cars")
    public ResponseEntity<ApiResponse<List<CarDTO>>> getAllCars(){
          List<CarDTO> carDTOList= customerService.findAllCars();

          return ResponseEntity.status(HttpStatus.OK).body(
                  new ApiResponse<>("Success", carDTOList));
    }

    @GetMapping("/cars-page")
    public ResponseEntity<ApiResponse<CarDTOPage>> findAllCars(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size){
             CarDTOPage carDTO = customerService.getAllCars(page, size);

             return ResponseEntity.status(HttpStatus.OK).body(
                     new ApiResponse<>("Success", carDTO));

    }
    @GetMapping("/car/{carId}")
    public ResponseEntity<ApiResponse<CarDTO>> findCarById(@PathVariable Long carId){

        CarDTO carDTO = customerService.getCarById(carId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>("Success", carDTO));
    }

    @PostMapping("/book-car")
    public ResponseEntity<ApiResponse<Boolean>> bookCar(@RequestBody @Valid BookingDetailsDTO bookingDetailsDTO){
        boolean isBooked = customerService.bookCar(bookingDetailsDTO);

        return isBooked ? ResponseEntity.status(HttpStatus.OK)
                       .body(new ApiResponse<>("Booking Successful")) :
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new ApiResponse<>("Booking Failed"));

    }
}
