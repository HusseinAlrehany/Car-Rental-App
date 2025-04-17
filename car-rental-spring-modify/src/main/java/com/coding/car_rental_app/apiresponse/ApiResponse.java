package com.coding.car_rental_app.apiresponse;

public record ApiResponse<T>(String message, T data) {

    public ApiResponse(String message){
        this(message, null);
    }
}
