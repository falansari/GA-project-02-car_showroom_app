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
     * Find all cars by their model id
     * @param carModelId Long
     * @return List of Car
     */
    List<Car> findByCarModelId(Long carModelId);

    /**
     * Find all vehicles belonging to a specific owner
     * @param ownerId Long
     * @return List of Car
     */
    List<Car> findAllByOwnerId(Long ownerId);

    /**
     * Find all cars of a specific model belonging to a specific owner
     * @param carModelId Long
     * @param ownerId Long
     * @return List of Car
     */
    List<Car> findAllByCarModelIdAndOwnerId(Long carModelId, Long ownerId);

    /**
     * Find out if a car exists by registration number
     * @param registrationNumber String
     * @return Boolean
     */
    Boolean existsByRegistrationNumber(String registrationNumber);

    /**
     * Find out if a car exists by insurance policy
     * @param insurancePolicy String
     * @return Boolean
     */
    Boolean existsByInsurancePolicy(String insurancePolicy);

    /**
     * Find out if a car exists by vin number
     * @param vinNumber String
     * @return Boolean
     */
    Boolean existsByVinNumber(String vinNumber);
}
