package com.coding.car_rental_app.dtos;

import lombok.Data;

import java.util.List;

//CarDTOPage to customize the only needed fields from Car returned per Page
//also to exclude not used returned fields which returned by default from using Page interface
@Data
public class CarDTOPage {

    private List<CarDTO> content;
    private int totalPages;
    private Long totalElements;
    private int size;
    private int number;
    private int numberOfElements;
}
