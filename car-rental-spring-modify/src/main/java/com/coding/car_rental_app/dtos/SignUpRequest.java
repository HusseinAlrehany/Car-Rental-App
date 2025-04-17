package com.coding.car_rental_app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true) //to ignore unknown fields send from frontEnd
public class SignUpRequest {

      private String name;
      private String email;
      private String password;
}
