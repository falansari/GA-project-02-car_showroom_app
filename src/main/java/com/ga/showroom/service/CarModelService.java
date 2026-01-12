package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Year;
import java.util.List;

@Service
@RequestMapping(path = "api/car-models")
public class CarModelService {
    CarModelRepository carModelRepository;

    @Autowired
    public void setCarModelRepository(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    /**
     * Find car model by ID
     * @param carModelId Long
     * @return CarModel
     */
    public CarModel getCarModelById(Long carModelId) {
        return carModelRepository.findById(carModelId)
                .orElseThrow(() -> new InformationNotFoundException("Car Model with ID " + carModelId + " not found"));
    }

    /**
     * Find car model by model name
     * @param carModelName String
     * @return CarModel
     */
    public CarModel getCarModelByName(String carModelName) {
        return carModelRepository.findByName(carModelName);
    }

    /**
     * Find all car models
     * @return List of CarModel
     */
    public List<CarModel> getAllCarModels() {
        return carModelRepository.findAll();
    }

    /**
     * Find all car models belonging to a specific make year
     * @param makeYear Year
     * @return List of CarModel
     */
    public List<CarModel> getAllByMakeYear(Year makeYear) {
        return carModelRepository.findAllByMakeYear(makeYear);
    }

    /**
     * Find all car models created between start and end make year.
     * @param start Year
     * @param end Year
     * @return List of CarModel
     */
    public List<CarModel> getAllByMakeYearBetween(Year start, Year end) {
        return carModelRepository.findAllByMakeYearBetween(start, end);
    }

    /**
     * Find all car models belonging to a specific manufacturer
     * @param manufacturer String
     * @return List of CarModel
     */
    public List<CarModel> getAllByManufacturer(String manufacturer) {
        return carModelRepository.findAllByManufacturer(manufacturer);
    }

    /**
     * Find all car options belonging to a car model
     * @param carModelId Long
     * @return List of Option
     */
    public List<Option> getAllCarModelOptions(Long carModelId) {
        return carModelRepository.findAllOptionsByCarModelId(carModelId);
    }

    /**
     * Find all cars with of a car model
     * @param carModelId Long
     * @return List of Car
     */
    public List<Car> getAllCarsByCarModelId(Long carModelId) {
        return carModelRepository.findAllCarsByCarModelId(carModelId);
    }
}
