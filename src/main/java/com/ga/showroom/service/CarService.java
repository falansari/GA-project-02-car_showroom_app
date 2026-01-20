package com.ga.showroom.service;

import com.ga.showroom.exception.AccessDeniedException;
import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.*;
import com.ga.showroom.model.enums.Role;
import com.ga.showroom.repository.CarRepository;
import com.ga.showroom.utility.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

@Service
public class CarService {
    CarRepository carRepository;
    Uploads uploads;
    final String uploadPath = "uploads/model-images"; // TODO: update to use cars' own car-images route if generate image feature added

    @Autowired
    public CarService(CarRepository carRepository,  Uploads uploads) {
        this.carRepository = carRepository;
        this.uploads = uploads;
    }

    /**
     * Find car by ID
     * @param carId Long
     * @return Car
     */
    public Car getCarById(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new InformationNotFoundException("Car with ID " + carId + " not found"));

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !car.getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return car;
    }

    /**
     * Find car by registration number
     * @param registrationNumber String
     * @return Car
     */
    public Car getByRegistrationNumber(String registrationNumber) {
        Car car = carRepository.findByRegistrationNumber(registrationNumber);

        if (car == null) throw new InformationNotFoundException("Car with registration number " + registrationNumber + " not found");

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !car.getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return car;
    }

    /**
     * Find car by insurance policy
     * @param insurancePolicy String
     * @return Car
     */
    public Car getByInsurancePolicy(String insurancePolicy) {
        Car car = carRepository.findByInsurancePolicy(insurancePolicy);

        if (car == null) throw new InformationNotFoundException("Car with insurance policy " + insurancePolicy + " not found");

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !car.getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return car;
    }

    /**
     * Find car by vehicle vin number
     * @param vinNumber String
     * @return Car
     */
    public Car getByVinNumber(String vinNumber) {
        Car car = carRepository.findByVinNumber(vinNumber);

        if (car == null) throw new InformationNotFoundException("Car with vin " + vinNumber + " not found");

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !car.getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return car;
    }

    /**
     * Get all cars in storage
     * @return List of Car
     */
    public List<Car> getAllCars() {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Customer views own data only
            return carRepository.findAllByOwnerId(getCurrentLoggedInUser().getId());

        return carRepository.findAll();
    }

    /**
     * Get all cars belonging to a specific model
     * @param carModelId Long
     * @return List of Car
     */
    public List<Car> getByCarModelId(Long carModelId) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Customer views own data only
            return carRepository.findAllByCarModelIdAndOwnerId(carModelId, getCurrentLoggedInUser().getId());

        return carRepository.findByCarModelId(carModelId);
    }

    /**
     * Create a new purchased vehicle.
     * @param car Car
     * @param owner User
     * @param carModel CarModel
     * @param image String generated car image's filename [PNG, JPEG, JPG]
     * @param carOptions List of CarOption
     * @param order Order
     * @return Car
     */
    public Car createCar(Car car, User owner, CarModel carModel, String image, List<CarOption> carOptions, Order order) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Only a salesman or admin may create new orders that contain new car data
            throw new AccessDeniedException("You are not authorized to create new cars. Please contact a salesman or admin.");

        if (carRepository.existsByRegistrationNumber(car.getRegistrationNumber()))
            throw new InformationExistException("Car with registration number " + car.getRegistrationNumber() + " already exists");

        if (carRepository.existsByInsurancePolicy(car.getInsurancePolicy()))
            throw new InformationExistException("Car with insurance policy " + car.getInsurancePolicy() + " already exists");

        if (carRepository.existsByVinNumber(car.getVinNumber()))
            throw new InformationExistException("Car with vin " + car.getVinNumber() + " already exists");


        car.setOwner(owner);
        car.setCarModel(carModel);

        if (image != null) car.setImage(image);

        if (!carOptions.isEmpty()) car.setCarOptions(carOptions);

        car.setOrder(order);

        return carRepository.save(car);
    }

    /**
     * Patch update existing car's registration number, insurance policy, and/or owner.
     * @param carId Long
     * @param car Car
     * @param owner User
     * @return Car
     */
    public Car updateCar(Long carId, Car car, User owner) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Only a salesman or admin may create new orders that contain new car data
            throw new AccessDeniedException("You are not authorized to update car details. Please contact a salesman or admin.");

        Car updatedCar = getCarById(carId);

        if (updatedCar == null) throw new InformationNotFoundException("Car with ID " + carId + " not found");

        if (car.getRegistrationNumber() != null) {
            Car existing = getByRegistrationNumber(car.getRegistrationNumber());

            if (!Objects.equals(existing.getId(), updatedCar.getId()))
                throw new InformationExistException("Car with Registration Number " + existing.getRegistrationNumber() + " already exists");

            updatedCar.setRegistrationNumber(car.getRegistrationNumber());
        }

        if (car.getInsurancePolicy() != null) {
            Car existing = getByInsurancePolicy(car.getInsurancePolicy());

            if (!Objects.equals(existing.getId(), updatedCar.getId()))
                throw new InformationExistException("Car with Insurance Policy " + existing.getInsurancePolicy() + " already exists");

            updatedCar.setInsurancePolicy(car.getInsurancePolicy());
        }

        if (owner != updatedCar.getOwner()) updatedCar.setOwner(owner);

        return carRepository.save(updatedCar);
    }

    /**
     * Download stored car's image
     * @param carId Long
     * @return ResponseEntity Resource The stored image if any [PNG, JPEG]
     */
    public ResponseEntity<Resource> downloadCarImage(Long carId) {
        Car car = getCarById(carId);

        if (car == null) throw new InformationNotFoundException("Car with ID " + carId + " not found");

        return uploads.downloadImage(uploadPath, car.getImage());
    }
}
