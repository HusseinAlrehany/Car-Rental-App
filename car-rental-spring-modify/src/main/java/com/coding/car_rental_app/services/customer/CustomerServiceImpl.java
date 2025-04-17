package com.coding.car_rental_app.services.customer;

import com.coding.car_rental_app.dtos.BookingDetailsDTO;
import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.dtos.CarDTOPage;
import com.coding.car_rental_app.entity.BookingDetails;
import com.coding.car_rental_app.entity.Car;
import com.coding.car_rental_app.entity.User;
import com.coding.car_rental_app.enums.BookingStatus;
import com.coding.car_rental_app.exceptions.MinimumDurationInDaysException;
import com.coding.car_rental_app.exceptions.ValidationException;
import com.coding.car_rental_app.mapper.Mapper;
import com.coding.car_rental_app.repository.BookingDetailsRepository;
import com.coding.car_rental_app.repository.CarRepository;
import com.coding.car_rental_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BookingDetailsRepository bookingDetailsRepository;

    private final Mapper mapper;

    @Override
    public List<CarDTO> findAllCars() {

        //optional.of -> 100% sure that cars will not be null(but if so it will throw null pointer exception)
        //optional.ofNullable -> the value may be null so it returns empty box(not throw null pointer exception)
        List<Car> cars = carRepository.findAll();
        //no need to use optional.of because spring data jpa guarantee that
        //cars never be null but may be empty
        if(cars.isEmpty()){
            throw new ValidationException("No Cars Found!");
        }

        return cars.stream().map(mapper::getCarDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "cars", key = "#page + '' + #size")
    @Override
    public CarDTOPage getAllCars(int page, int size) {
        //no use of optional because page, size is primitives so it will never be null
        if(page < 0){
            throw new ValidationException("Invalid page");
        }
        if(size <= 0 ){
            throw new ValidationException("Invalid size");
        }

        System.out.println("Fetching cars from db....");
        //no use of optional here because lists are by default optional
        Page<Car> cars = carRepository.findAll(PageRequest.of(page, size));
          if(!cars.hasContent()){
              throw new ValidationException("No Cars Found!");
          }

        return mapper.getCarDTOPage(cars);
    }

    @Cacheable(value = "car", key = "#carId")
    @Override
    public CarDTO getCarById(Long carId){

        if(carId < 0 ){
            throw new ValidationException("Invalid carId");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(()-> new ValidationException("No Car Found!"));

        return mapper.getCarDTO(car);
    }

    @Override
    public boolean bookCar(BookingDetailsDTO bookingDetailsDTO) {

        Optional<Car> optionalCar = carRepository.findById(bookingDetailsDTO.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookingDetailsDTO.getUserId());

        long duration = bookingDetailsDTO.getToDate().getTime() - bookingDetailsDTO.getFromDate().getTime();
        long durationInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if(durationInDays < 1){
            throw new MinimumDurationInDaysException("Minimum Duration in days is one day");
        }

        if(optionalCar.isEmpty() || optionalUser.isEmpty()){
            throw new ValidationException("No user or Car Found!");

        }

        BookingDetails bookingDetails = new BookingDetails();

        bookingDetails.setBookingStatus(BookingStatus.PENDING);
        bookingDetails.setPrice(optionalCar.get().getPrice() * durationInDays);
        bookingDetails.setCar(optionalCar.get());
        bookingDetails.setUser(optionalUser.get());
        bookingDetails.setFromDate(bookingDetailsDTO.getFromDate());
        bookingDetails.setToDate(bookingDetailsDTO.getToDate());
        bookingDetails.setDays(durationInDays);

        bookingDetailsRepository.save(bookingDetails);

        return true;
    }
}
