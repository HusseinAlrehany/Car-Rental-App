package com.coding.car_rental_app.exceptions;

public class RecurrentUpdateException extends RuntimeException{

    public RecurrentUpdateException(String message) {
        super(message);
    }

    public RecurrentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecurrentUpdateException(Throwable cause) {
        super(cause);
    }
}
