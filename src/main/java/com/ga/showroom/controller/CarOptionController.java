package com.ga.showroom.controller;

import com.ga.showroom.model.CarOption;
import com.ga.showroom.service.CarOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/car-options")
public class CarOptionController {
    private CarOptionService carOptionService;

    @Autowired
    public void setCarOptionService(CarOptionService carOptionService) {
        this.carOptionService = carOptionService;
    }

    /**
     * Get all car options
     * @see GetMapping api/car-options
     * @return List of CarOption
     */
    @GetMapping("")
    public List<CarOption> getAllCarOptions() {
        return carOptionService.getAll();
    }

    /**
     * Get a car option by its ID
     * @param carOptionId Long
     * @see GetMapping api/car-options/{carOptionId}
     * @return CarOption
     */
    @GetMapping("/{carOptionId}")
    public CarOption getCarOptionById(@PathVariable("carOptionId") Long carOptionId) {
        return carOptionService.getById(carOptionId);
    }

    /**
     * Get a car option by its option ID and car ID
     * @param optionId Long
     * @param carId Long
     * @see GetMapping api/car-options/option/{optionId}/car/{carId}
     * @return CarOption
     */
    @GetMapping("/option/{optionId}/car/{carId}")
    public CarOption getCarOptionByOptionIdAndCarId(@PathVariable("optionId") Long optionId, @PathVariable("carId") Long carId) {
        return carOptionService.getByOptionIdAndCarId(optionId, carId);
    }

    /**
     * Get list of car options belonging to an option by its ID
     * @param optionId Long
     * @see GetMapping api/car-options/option/{optionId}
     * @return List of CarOption
     */
    @GetMapping("/option/{optionId}")
    public List<CarOption> getCarOptionsByOptionId(@PathVariable("optionId") Long optionId) {
        return carOptionService.getAllByOptionId(optionId);
    }

    /**
     * Get list of car options belonging to a car by its ID
     * @param carId Long
     * @see GetMapping api/car-options/car/{carId}
     * @return List of CarOption
     */
    @GetMapping("/car/{carId}")
    public List<CarOption> getCarOptionsByCarId(@PathVariable("carId") Long carId) {
        return carOptionService.getAllByCarId(carId);
    }

    /**
     * Create a new car option. Must be a unique option and car combination.
     * @param optionId Long
     * @param carId Long
     * @see PostMapping api/car-options
     * @return CarOption
     */
    @PostMapping("")
    public CarOption createCarOption(@RequestParam("optionId") Long optionId, @RequestParam("carId") Long carId) {
        return carOptionService.createCarOption(optionId, carId);
    }
}
