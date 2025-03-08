package com.coding.car_rental_app.dtos;

import lombok.Data;

@Data
public class SigninRequest {

    private String email;
    private String password;
}
