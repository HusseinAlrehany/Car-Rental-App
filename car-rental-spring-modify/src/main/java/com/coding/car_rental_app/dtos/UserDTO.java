package com.coding.car_rental_app.dtos;

import com.coding.car_rental_app.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
}
