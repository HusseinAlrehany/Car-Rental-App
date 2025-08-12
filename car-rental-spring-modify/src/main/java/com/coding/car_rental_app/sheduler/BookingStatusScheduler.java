package com.coding.car_rental_app.sheduler;

import com.coding.car_rental_app.entity.BookingDetails;
import com.coding.car_rental_app.enums.BookingStatus;
import com.coding.car_rental_app.repository.BookingDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
//spring boot schedule task done every day at 9:20PM
@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final BookingDetailsRepository  bookingDetailsRepository;

    //checks every day at 9:14 PM
    @Scheduled(cron = "0 20 21 * * *", zone = "Africa/Cairo")
    public void markExpiredBookingsAsCompleted(){
        Date today = new Date();
       int rowAffected = bookingDetailsRepository
                 .markAllExpiredBookingsAsCompleted(today);

       if(rowAffected > 0){
           System.out.println("Expired Journey " + rowAffected + " NOW COMPLETED");
       }

    }
}
