package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
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
}
