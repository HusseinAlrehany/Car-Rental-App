package com.coding.car_rental_app.dtos;

import com.coding.car_rental_app.exceptions.ImageCustomValidationException.ImgValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public class CarDTO {


    private Long id;
    @NotBlank(message = "Brand can not be null")
    private String brand;
    @NotBlank(message = "Color can not be blank")
    private String color;
    @NotBlank(message = "Name can not be blank")
    private String name;
    @NotBlank(message = "Type can not blank")
    private String type;
    @NotBlank(message = "Transmission can not be blank")
    private String transmission;
    @Size(max = 500, message = "Description can not exceed 500 character")
    private String description;
    @Min(value = 0, message = "Price can not be negative")
    private Long price;
    @PastOrPresent(message = "the model should be present year or past year")
    private Date modelYear;

    private byte[] image;

    //custom validation annotation
    @ImgValidation
    private MultipartFile returnedImage;
}
