package com.coding.car_rental_app.services.admin;

import com.coding.car_rental_app.dtos.CarDTO;
import com.coding.car_rental_app.entity.Car;
import com.coding.car_rental_app.exceptions.ProcessingImgException;
import com.coding.car_rental_app.exceptions.ValidationException;
import com.coding.car_rental_app.mapper.Mapper;
import com.coding.car_rental_app.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class AdminServiceImpl implements  AdminService{

    private final CarRepository carRepository;

    private final Mapper mapper;
    @Override
    public CarDTO addCar(CarDTO carDTO) throws IOException {

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
        car.setImage(carDTO.getReturnedImage().getBytes());

         Car savedCar = carRepository.save(car);

        return mapper.getCarDTO(savedCar);
    }

    @Override
    public List<CarDTO> getAllCars() {
        List <Car> cars = carRepository.findAll();
        Optional.of(cars)
                .filter(car -> !car.isEmpty())
                .orElseThrow(()-> new ValidationException("No Cars Found!"));

        return cars.stream().map(mapper::getCarDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long carId) {
        Optional.of(carRepository.findById(carId))
                .filter(Optional::isPresent)
                .orElseThrow(()-> new ValidationException("No car found!"));

                 carRepository.deleteById(carId);
    }

    @Override
    public CarDTO findCarById(Long carId) {
        Optional.ofNullable(carId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Car Id not Valid"));

        Car car = carRepository.findById(carId)
                .orElseThrow(()-> new ValidationException("No Car Found!!"));

        return mapper.getCarDTO(car);
    }

    @Override
    public CarDTO updateCar(Long carId, CarDTO carDTO) {
        Optional.ofNullable(carId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid car Id"));

        Car car = carRepository.findById(carId).orElseThrow(
                ()-> new ValidationException("No Car Found"));

        if(carDTO.getImage() != null){
             try{
                 car.setImage(carDTO.getReturnedImage().getBytes());

             }catch(IOException ex){
                  throw new ProcessingImgException("Error processing img");
             }
        }

        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setType(carDTO.getType());
        car.setColor(carDTO.getColor());
        car.setPrice(carDTO.getPrice());
        car.setDescription(carDTO.getDescription());
        car.setTransmission(carDTO.getTransmission());
        car.setModelYear(carDTO.getModelYear());

        return mapper.getCarDTO(carRepository.save(car));
    }
}
