package com.coding.car_rental_app.dtos;

import lombok.Data;

@Data
public class SignUpRequest {

      private String name;
      private String email;
      private String password;
}
