package com.coding.car_rental_app.dtos;

import com.coding.car_rental_app.enums.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class BookingDetailsDTO {

 //this is flattened DTO contains the ids from other entities instead of whole object

    private Long id;

    @FutureOrPresent(message = "Present or future dates only allowed")
    private Date fromDate;

    @Future(message = "Future dates only allowed")
    private Date toDate;

    private Long days;

    private Long price;

    private BookingStatus bookingStatus;

    @Min(value = 0, message = "user Id can not be negative")
    private Long userId;
    @Min(value= 0, message = "Car Id can not be negative")
    private Long carId;
}
