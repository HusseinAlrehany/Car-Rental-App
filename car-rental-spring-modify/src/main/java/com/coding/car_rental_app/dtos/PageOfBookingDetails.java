package com.coding.car_rental_app.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PageOfBookingDetails {

    private List<BookingDetailsDTO> content;
    private Long totalElements;
    private int totalPages;
    private int size;
    private int number;
}
