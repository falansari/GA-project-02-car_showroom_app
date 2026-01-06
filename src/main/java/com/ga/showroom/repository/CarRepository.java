package com.ga.showroom.repository;

import com.ga.showroom.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    /**
     * Find a car by its registration number
     * @param registrationNumber String
     * @return Car
     */
    Car findByRegistrationNumber(String registrationNumber);

    /**
     * Find a car by its insurance policy number
     * @param insurancePolicy String
     * @return Car
     */
    Car findByInsurancePolicy(String insurancePolicy);

    /**
     * Find a car by its VIN number
     * @param vinNumber String
     * @return Car
     */
    Car findByVinNumber(String vinNumber);

    /**
     * Find all cars by their manufacturer's name
     * @param manufacturer String
     * @return List of Car
     */
    List<Car> findByManufacturer(String manufacturer);

    /**
     * Find all cars by their model id
     * @param modelId Long
     * @return List of Car
     */
    List<Car> findByModelId(Long modelId);

    /**
     * Find all cars by their manufacturer name and model name
     * @param manufacturer String
     * @param model String
     * @return List of Car
     */
    List<Car> findByManufacturerAndModel(String manufacturer, String model);
}
