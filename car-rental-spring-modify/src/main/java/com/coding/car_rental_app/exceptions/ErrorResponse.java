package com.coding.car_rental_app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timeStamp;

}
