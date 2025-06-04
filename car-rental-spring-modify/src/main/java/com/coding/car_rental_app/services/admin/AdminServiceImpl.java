package com.coding.car_rental_app.services.admin;

import com.coding.car_rental_app.dtos.BookingDetailsDTO;
import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.dtos.CarDTOPage;
import com.coding.car_rental_app.dtos.PageOfBookingDetails;
import com.coding.car_rental_app.entity.BookingDetails;
import com.coding.car_rental_app.entity.Car;
import com.coding.car_rental_app.enums.BookingStatus;
import com.coding.car_rental_app.exceptions.ProcessingImgException;
import com.coding.car_rental_app.exceptions.RecurrentUpdateException;
import com.coding.car_rental_app.exceptions.ValidationException;
import com.coding.car_rental_app.mapper.Mapper;
import com.coding.car_rental_app.repository.BookingDetailsRepository;
import com.coding.car_rental_app.repository.CarRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class AdminServiceImpl implements  AdminService{

    private final CarRepository carRepository;

    private final BookingDetailsRepository bookingDetailsRepository;

    private final Mapper mapper;

    private static final Duration COOL_DOWN_PERIOD = Duration.ofMinutes(5);

   // private final LocalValidatorFactoryBean validator;
    @CacheEvict(value = "pageOfCars", key = "#page + '' + #size", beforeInvocation = false, allEntries = true)
    @Override
    public CarDTO addCar(CarDTO carDTO) {

        Car car = mapper.getCar(carDTO);

        //save(car) uses EntityManager internally which starts a transaction
        //then commit it if no error occurs so no need for @Transactional here
        Car savedCar = carRepository.save(car);

        return mapper.getCarDTO(savedCar);
    }

    //no longer used as we use page of cars instead of it
    @Cacheable(value = "cars", key = "'allCars'")
    @Override
    public List<CarDTO> getAllCars() {
        System.out.println("Fetching from database ....");
        List <Car> cars = carRepository.findAll();
        if(cars.isEmpty()){
           throw new ValidationException("No Cars Found!");
        }

        return cars.stream().map(mapper::getCarDTO).collect(Collectors.toList());
    }


    @CacheEvict(value = "pageOfCars" , key = "#page + '' + #size", beforeInvocation = false, allEntries = true)
    @Transactional //to ensure consistency between success of delete and cache eviction
    @Override      //because if the delete fails without @Transactional it will evict the cache
    public void deleteCar(Long carId) {
          carRepository.findById(carId)
                  .orElseThrow(()-> new ValidationException("No Car Found!"));
          carRepository.deleteById(carId);
    }

    //#carId because the parameter is dynamic and will be changed
    //if there is many parameter (key = "#carId + '' + #type + '' + #model")
    @Cacheable(value = "ACar", key = "#carId")
    @Override
    public CarDTO findCarById(Long carId) {
        if(carId < 0){
            throw new ValidationException("Car Id not Valid");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(()-> new ValidationException("No Car Found!!"));

        return mapper.getCarDTO(car);
    }

    //(@Transactional)to ensure that all method steps is executed successfully or not at all(roll back)
    //use beforeInvocation = false -> to guarntee that cache will be cleared only if transaction is successful
    @CacheEvict(value = "pageOfCars", key = "#page + '' + #size", allEntries = true, beforeInvocation = false)
    @Transactional
    @Override
    public CarDTO updateCar(Long carId, CarDTO carDTO) {
        if(carId < 0){
            throw new ValidationException("Invalid car Id");
        }

        Car car = carRepository.findById(carId).orElseThrow(
                ()-> new ValidationException("No Car Found"));

        //to prevent recurrent update of the same car unless 5 minutes later
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUpdated = car.getLastUpdated();

        if(lastUpdated != null && Duration.between(lastUpdated, now).compareTo(COOL_DOWN_PERIOD) < 0){
            throw new RecurrentUpdateException("Car can not be updated soon, please wait 5 minutes then try again");
        }

        mapper.updateCar(car, carDTO);

        return mapper.getCarDTO(carRepository.save(car));
    }

    @Override
    public List<BookingDetailsDTO> getAllBookedCars() {
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findAll();
        if(bookingDetails.isEmpty()){
            throw new ValidationException("No Booking Found");
        }

        return bookingDetails.stream().map(mapper::getBookingDetailsDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "bookingDetailsPage", key = "#page + '' + #size")
    @Override
    public PageOfBookingDetails getBookingDetailsPage(int page, int size) {
        if(page < 0 || size <= 0 ){
            throw new ValidationException("Invalid page or size");
        }

        Page<BookingDetails> bookingDetailsPage = bookingDetailsRepository.findAll(PageRequest.of(page,size));

        if(!bookingDetailsPage.hasContent()){
            throw new ValidationException("No Booked Cars Found!");
        }

        return mapper.getBookingDetailsPage(bookingDetailsPage);
    }


    @CacheEvict(value = "bookingDetailsPage", key = "#page + '' + #size", allEntries = true, beforeInvocation = false)
    @Override
    public BookingDetailsDTO changeBookingStatus(Long bookingId, String status) {
         Optional<BookingDetails> optionalBookingDetails = bookingDetailsRepository.findById(bookingId);
         if(optionalBookingDetails.isEmpty()){
             throw new ValidationException("No Booking Details Found");
         }

         BookingDetails bookingDetails = optionalBookingDetails.get();

         BookingStatus bookingStatus = BookingStatus.fromStringToBookingStatus(status);

         bookingDetails.setBookingStatus(bookingStatus);

        return mapper.getBookingDetailsDTO(bookingDetailsRepository.save(bookingDetails));
    }

    //the result of the method will be stored under the value "pageOfCars"
    @Cacheable(value = "pageOfCars", key = "#page + '' + #size")
    @Override
    public CarDTOPage pageOfCars(int page, int size) {

        if(page < 0 || size <= 0){
            throw new ValidationException("Invalid page or size");
        }

        Page<Car> carPage = carRepository.findAll(PageRequest.of(page, size));
        if(!carPage.hasContent()){
            throw new ValidationException("No Cars Found!");
        }

        return mapper.getCarDTOPage(carPage);
    }
}
