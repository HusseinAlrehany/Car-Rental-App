package com.coding.car_rental_app.mapper;

import com.coding.car_rental_app.dtos.*;
import com.coding.car_rental_app.entity.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Mapper {


    public UserDTO getUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());

        return userDTO;
    }

    public CarDTO getCarDTO(Car car){
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        carDTO.setBrand(car.getBrand());
        carDTO.setColor(car.getColor());
        carDTO.setPrice(car.getPrice());
        carDTO.setType(car.getType());
        carDTO.setDescription(car.getDescription());
        carDTO.setModelYear(car.getModelYear());
        carDTO.setTransmission(car.getTransmission());
        carDTO.setImage(car.getImage());

        return carDTO;
    }


}
