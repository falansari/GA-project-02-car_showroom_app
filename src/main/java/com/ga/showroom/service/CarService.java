package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.*;
import com.ga.showroom.repository.CarRepository;
import com.ga.showroom.utility.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class CarService {
    CarRepository carRepository;
    Uploads uploads;

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
        return carRepository.findById(carId)
                .orElseThrow(() -> new InformationNotFoundException("Car with ID " + carId + " not found"));
    }

    /**
     * Find car by registration number
     * @param registrationNumber String
     * @return Car
     */
    public Car getByRegistrationNumber(String registrationNumber) {
        Car car = carRepository.findByRegistrationNumber(registrationNumber);

        if (car == null) throw new InformationNotFoundException("Car with registration number " + registrationNumber + " not found");
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
        return car;
    }

    /**
     * Get all cars in storage
     * @return List of Car
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Get all cars belonging to a specific model
     * @param carModelId Long
     * @return List of Car
     */
    public List<Car> getByCarModelId(Long carModelId) {
        return carRepository.findByCarModelId(carModelId);
    }

    /**
     * Create a new purchased vehicle.
     * @param car Car
     * @param customer User
     * @param carModel CarModel
     * @param image MultipartFile [PNG, JPEG, JPG]
     * @param carOptions List of CarOption
     * @param orderLine OrderLine
     * @return Car
     */
    public Car createCar(Car car, User customer, CarModel carModel, MultipartFile image, List<CarOption>  carOptions, OrderLine orderLine) {
        if (carRepository.existsByRegistrationNumber(car.getRegistrationNumber()))
            throw new InformationExistException("Car with registration number " + car.getRegistrationNumber() + " already exists");

        if (carRepository.existsByInsurancePolicy(car.getInsurancePolicy()))
            throw new InformationExistException("Car with insurance policy " + car.getInsurancePolicy() + " already exists");

        if (carRepository.existsByVinNumber(car.getVinNumber()))
            throw new InformationExistException("Car with vin " + car.getVinNumber() + " already exists");

        car.setOwner(customer);
        car.setCarModel(carModel);

        String uploadedImage = uploads.uploadImage("uploads/car-images", image);

        if (uploadedImage != null) car.setImage(uploadedImage);

        if (!carOptions.isEmpty()) car.setCarOptions(carOptions);

        car.setOrderLine(orderLine);

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
}
