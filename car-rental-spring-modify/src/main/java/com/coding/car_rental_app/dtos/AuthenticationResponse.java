package com.coding.car_rental_app.dtos;

import com.coding.car_rental_app.enums.UserRole;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class AuthenticationResponse {

    private String jwtToken;
    private Long userId;
    private UserRole userRole;
}
