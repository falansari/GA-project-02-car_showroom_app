package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarOption;
import com.ga.showroom.model.Option;
import com.ga.showroom.repository.CarOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarOptionService {
    CarOptionRepository carOptionRepository;
    CarService carService;
    OptionService optionService;

    @Autowired
    public CarOptionService(CarOptionRepository carOptionRepository,  CarService carService, OptionService optionService) {
        this.carOptionRepository = carOptionRepository;
        this.carService = carService;
        this.optionService = optionService;
    }

    /**
     * Get a car's option by its ID
     * @param carOptionId Long
     * @return CarOption
     */
    public CarOption getById(Long carOptionId) {
        return carOptionRepository.findById(carOptionId)
                .orElseThrow(() -> new InformationNotFoundException("Car option with ID " + carOptionId + " not found"));
    }

    /**
     * Find a car option by the IDs of the option and car it belongs to. A unique combination.
     * @param optionId Long
     * @param carId Long
     * @return CarOption
     */
    public CarOption getByOptionIdAndCarId(Long optionId, Long carId) {
        CarOption carOption = carOptionRepository.findByOptionIdAndCarId(optionId, carId);

        if (carOption == null)
            throw new InformationNotFoundException("Car option with Option ID " + optionId + " and Car ID " + carId + " not found");

        return carOption;
    }

    /**
     * Get all car options in database
     * @return List of CarOption
     */
    public List<CarOption> getAll() {
        return carOptionRepository.findAll();
    }

    /**
     * Get all car options belonging to a specific option
     * @param optionId Long
     * @return List of CarOption
     */
    public List<CarOption> getAllByOptionId(Long optionId) {
        return carOptionRepository.findAllByOptionId(optionId);
    }

    /**
     * Get all car options belonging to a specific car
     * @param carId Long
     * @return List of CarOption
     */
    public List<CarOption> getAllByCarId(Long carId) {
        return carOptionRepository.findAllByCarId(carId);
    }

    public CarOption createCarOption(Long optionId, Long carId) {
        Option option = optionService.getOptionById(optionId);
        if (option == null) throw new InformationNotFoundException("Option not found. Please provide a valid option ID");

        Car car = carService.getCarById(carId);
        if (car == null) throw new InformationNotFoundException("Car not found. Please provide a valid car ID");

        CarOption existing = carOptionRepository.findByOptionIdAndCarId(optionId, carId);
        if (existing != null)
            throw new InformationExistException("Car option with option ID " + option.getId() +
                    " and car ID " + car.getId() + " already exists");

        CarOption carOption = new CarOption();
        carOption.setOption(option);
        carOption.setCar(car);

        return carOptionRepository.save(carOption);
    }
}
