package com.coding.car_rental_app.enums;

import com.coding.car_rental_app.exceptions.ValidationException;

import java.util.Arrays;

public enum BookingStatus {
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED;


    public static BookingStatus fromStringToBookingStatus(String value){
        return Arrays.stream(BookingStatus.values())
                .filter(status-> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(()-> new ValidationException("Invalid Booking Status"));

    }

}
