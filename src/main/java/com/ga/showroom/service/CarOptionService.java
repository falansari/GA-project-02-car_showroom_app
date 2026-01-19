package com.ga.showroom.service;

import com.ga.showroom.exception.AccessDeniedException;
import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarOption;
import com.ga.showroom.model.Option;
import com.ga.showroom.model.enums.Role;
import com.ga.showroom.repository.CarOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

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
        CarOption carOption = carOptionRepository.findById(carOptionId)
                .orElseThrow(() -> new InformationNotFoundException("Car option with ID " + carOptionId + " not found"));

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !carOption.getCar().getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return carOption;
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

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !carOption.getCar().getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return carOption;
    }

    /**
     * Get all car options in database
     * @return List of CarOption
     */
    public List<CarOption> getAll() {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Customer views their own data only
            return carOptionRepository.findAllByCarOwnerId(getCurrentLoggedInUser().getId());

        return carOptionRepository.findAll();
    }

    /**
     * Get all car options belonging to a specific option
     * @param optionId Long
     * @return List of CarOption
     */
    public List<CarOption> getAllByOptionId(Long optionId) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Customer views their own data only
            return carOptionRepository.findAllByOptionIdAndCarOwnerId(optionId, getCurrentLoggedInUser().getId());

        return carOptionRepository.findAllByOptionId(optionId);
    }

    /**
     * Get all car options belonging to a specific car
     * @param carId Long
     * @return List of CarOption
     */
    public List<CarOption> getAllByCarId(Long carId) {
        Car car = carService.getCarById(carId);

        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER) // not owner customer not allowed to view data
                && !car.getOwner().getId().equals(getCurrentLoggedInUser().getId()))
            throw new AccessDeniedException("You are not authorized to view this vehicle's information. " +
                    "Please contact a salesman or the vehicle's owner.");

        return carOptionRepository.findAllByCarId(carId);
    }

    /**
     * Add an option to a car
     * @param optionId Long
     * @param carId Long
     * @return CarOption
     */
    public CarOption createCarOption(Long optionId, Long carId) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER)) // Only a salesman or admin may create new orders that contain new car data
            throw new AccessDeniedException("You are not authorized to create new car options. Please contact a salesman or admin.");

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
