package com.coding.car_rental_app.repository;

import com.coding.car_rental_app.entity.BookingDetails;
import com.coding.car_rental_app.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {

    Page<BookingDetails> findAllByUserId(Long userId, Pageable pageable);

    List<BookingDetails> findByToDateBeforeAndBookingStatus(Date today, BookingStatus bookingStatus);

    boolean existsByUserIdAndCarIdAndBookingStatusIn(Long userId, Long CarId,
                                                    List<BookingStatus> status );


    //to ensure that no overlapping in booking car
    //to prevent user from booking the same car at the same period as another user did
    @Query("SELECT b FROM BookingDetails b WHERE b.car.id = :carId " +
            "AND :fromDate <= b.toDate AND :toDate >= b.fromDate " +
            "AND b.bookingStatus = 'APPROVED'")
    List<BookingDetails> findOverlappingBookings(
            @Param("carId") Long carId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);
}
