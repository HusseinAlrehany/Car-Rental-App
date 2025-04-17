package com.coding.car_rental_app.mapper;

import com.coding.car_rental_app.dtos.*;
import com.coding.car_rental_app.entity.*;
import com.coding.car_rental_app.exceptions.ProcessingImgException;
import com.coding.car_rental_app.exceptions.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

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

    public CarDTOPage getCarDTOPage(Page <Car> carPage){
        CarDTOPage carDTOPage = new CarDTOPage();
        carDTOPage.setNumber(carPage.getNumber());
        carDTOPage.setTotalPages(carPage.getTotalPages());
        carDTOPage.setSize(carPage.getSize());
        carDTOPage.setTotalElements(carPage.getTotalElements());
        carDTOPage.setNumberOfElements(carPage.getNumberOfElements());
        carDTOPage.setContent(carPage.getContent().stream().map(this::getCarDTO).collect(Collectors.toList()));

        return carDTOPage;
    }

    //to update existing car without creating new object(getCar(CarDTO carDto))
    //more efficient in terms of memory and performance
    //data integrity Updating the existing object ensures that relationships
    // with other entities are preserved, reducing the risk of data inconsistencies
    public void updateCar(Car car,CarDTO carDTO) {

        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setColor(carDTO.getColor());
        car.setPrice(carDTO.getPrice());
        car.setType(carDTO.getType());
        car.setDescription(carDTO.getDescription());
        car.setModelYear(carDTO.getModelYear());
        car.setTransmission(carDTO.getTransmission());

        if(carDTO.getReturnedImage() != null){
            car.setImage(convertImageToByte(carDTO.getReturnedImage()));
        }

    }

    public Car getCar(CarDTO carDTO) {

        Car car = new Car();
        car.setId(carDTO.getId());
        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setColor(carDTO.getColor());
        car.setPrice(carDTO.getPrice());
        car.setType(carDTO.getType());
        car.setDescription(carDTO.getDescription());
        car.setModelYear(carDTO.getModelYear());
        car.setTransmission(carDTO.getTransmission());

        if(carDTO.getReturnedImage() != null){
            car.setImage(convertImageToByte(carDTO.getReturnedImage()));
        }
    return car;
    }

    //handling convert from multipart to byte array
    public byte[] convertImageToByte(MultipartFile img){
        try{
            return img.getBytes();
        }catch(IOException ex){
            throw new ProcessingImgException("Error processing image");
        }
    }

    public BookingDetailsDTO getBookingDetailsDTO(BookingDetails bookingDetails){

        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();

        bookingDetailsDTO.setId(bookingDetails.getId());
        bookingDetailsDTO.setBookingStatus(bookingDetails.getBookingStatus());
        bookingDetailsDTO.setPrice(bookingDetails.getPrice());
        bookingDetailsDTO.setFromDate(bookingDetails.getFromDate());
        bookingDetailsDTO.setToDate(bookingDetails.getToDate());
        bookingDetailsDTO.setDays(bookingDetails.getDays());

        bookingDetailsDTO.setCarId(bookingDetails.getCar().getId());
        bookingDetailsDTO.setUserId(bookingDetails.getUser().getId());

        return bookingDetailsDTO;
    }


}
