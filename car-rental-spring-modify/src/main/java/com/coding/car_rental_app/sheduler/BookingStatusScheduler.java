package com.coding.car_rental_app.sheduler;

import com.coding.car_rental_app.entity.BookingDetails;
import com.coding.car_rental_app.enums.BookingStatus;
import com.coding.car_rental_app.repository.BookingDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
//spring boot schedule task done every day.
@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final BookingDetailsRepository  bookingDetailsRepository;

    @Scheduled(cron = "0 0 0 * * *") //checks every midnight
    public void markExpiredBookingsAsCompleted(){
        Date today = new Date();
        List <BookingDetails> expiredBookings = bookingDetailsRepository
                .findByToDateBeforeAndBookingStatus(today, BookingStatus.APPROVED);

        for(BookingDetails booking: expiredBookings){
            booking.setBookingStatus(BookingStatus.COMPLETED);
        }

        bookingDetailsRepository.saveAll(expiredBookings);
    }
}
