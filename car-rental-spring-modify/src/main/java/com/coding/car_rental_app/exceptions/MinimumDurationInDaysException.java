package com.coding.car_rental_app.exceptions;

public class MinimumDurationInDaysException extends RuntimeException{

    public MinimumDurationInDaysException(String message) {
        super(message);
    }

    public MinimumDurationInDaysException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinimumDurationInDaysException(Throwable cause) {
        super(cause);
    }
}
