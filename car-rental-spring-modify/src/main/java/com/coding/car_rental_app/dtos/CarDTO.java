package com.coding.car_rental_app.dtos;

import com.coding.car_rental_app.validation.dtogroupvalidator.OnCreate;
import com.coding.car_rental_app.validation.dtogroupvalidator.OnUpdate;
import com.coding.car_rental_app.validation.imgcustomvalidation.ImgValidation;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

//group validation because the same dto has different validation logic
//in insert and in update operations, like a field must be not null in insert but it can be null in update
@Data
public class CarDTO {

    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Brand is required")
    private String brand;

    @NotBlank(groups = {OnCreate.class},message = "Color is required")
    private String color;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},message = "Name of the car is required")
    private String name;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},message = "Fuel Type is required")
    private String type;

    @NotBlank(groups = {OnCreate.class},message = "Transmission is required")
    private String transmission;

    @Size(max = 500, message = "Description can not exceed 500 character")
    private String description;

    @Min(groups = {OnCreate.class},value = 0, message = "Price can not be negative")
    @NotNull(groups = {OnCreate.class, OnUpdate.class},message = "Price is required")
    private Long price;

    @PastOrPresent(groups = {OnCreate.class}, message = "the model should be present year or past year")
    @NotNull(groups = {OnCreate.class}, message = "Model is required")
    private Date modelYear;

    //@JsonIgnore
    private byte[] image;

    //custom validation annotation for validating the img
    @ImgValidation(groups = {OnCreate.class, OnUpdate.class})
    private MultipartFile returnedImage;
}
