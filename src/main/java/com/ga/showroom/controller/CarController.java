package com.ga.showroom.controller;

import com.ga.showroom.model.Car;
import com.ga.showroom.service.CarService;
import com.ga.showroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/cars")
public class CarController {
    private CarService carService;
    private UserService userService;

    @Autowired
    public void setCarService(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    /**
     * Get car by its ID
     * @param carId Long
     * @return Car
     */
    @GetMapping("/{carId}")
    public Car getCarById(@PathVariable Long carId) {
        return carService.getCarById(carId);
    }

    /**
     * Find a car by its registration number
     * @param registrationNumber String
     * @return Car
     */
    @GetMapping("/registration/{registrationNumber}")
    public Car getCarByRegistrationNumber(@PathVariable("registrationNumber") String registrationNumber) {
        return carService.getByRegistrationNumber(registrationNumber);
    }

    /**
     * Find a car by its insurance policy number
     * @param insurancePolicy String
     * @return Car
     */
    @GetMapping("/insurance/{insurancePolicy}")
    public Car getCarByInsurancePolicy(@PathVariable("insurancePolicy") String insurancePolicy) {
        return carService.getByInsurancePolicy(insurancePolicy);
    }

    /**
     * Find a car by its vin number
     * @param vinNumber String
     * @return Car
     */
    @GetMapping("/vin/{vinNumber}")
    public Car getCarByVinNumber(@PathVariable("vinNumber") String vinNumber) {
        return carService.getByVinNumber(vinNumber);
    }

    /**
     * Get all cars in storage
     * @return List of Car
     */
    @GetMapping("")
    public List<Car> getCars() {
        return carService.getAllCars();
    }

    /**
     * Get all cars belonging to a specific model.
     * @param modelId Long
     * @return List of Car
     */
    @GetMapping("/model/{modelId}")
    public List<Car> getCarsByModel(@PathVariable("modelId") Long modelId) {
        return carService.getByCarModelId(modelId);
    }

    /**
     * Update existing vehicle's registration number, insurance policy, or owner.
     * @param carId Long
     * @param car Car
     * @param ownerEmail String
     * @return Car
     */
    @PatchMapping("/{carId}")
    public Car updateCar(@PathVariable Long carId, @RequestBody Car car, @RequestParam("email") String ownerEmail) {
        return carService.updateCar(carId, car, userService.findUserByEmailAddress(ownerEmail));
    }
}
