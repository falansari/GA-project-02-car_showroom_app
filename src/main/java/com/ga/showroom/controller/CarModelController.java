package com.ga.showroom.controller;

import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.service.CarModelService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping(path = "api/car-models")
public class CarModelController {
    private CarModelService carModelService;

    @Autowired
    public void setCarModelService(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    /**
     * Find car model by its ID
     * @param carModelId Long
     * @return CarModel
     */
    @GetMapping("/{carModelId}")
    public CarModel getCarModelById(@PathVariable("carModelId") long carModelId) {
        return carModelService.getCarModelById(carModelId);
    }

    /**
     * Find car model by its name
     * @param carModelName String
     * @return CarModel
     */
    @GetMapping("/name/{carModelName}")
    public CarModel getCarModelByName(@PathVariable("carModelName") String carModelName) {
        return carModelService.getCarModelByName(carModelName);
    }

    /**
     * Get all car models list
     * @return List of CarModel
     */
    @GetMapping("")
    public List<CarModel> getAllCarModels() {
        return carModelService.getAllCarModels();
    }

    /**
     * Get all car models by their make year
     * @param year Year
     * @return List of CarModel
     */
    @GetMapping("/year/{year}")
    public List<CarModel> getAllCarModelsByYear(@PathVariable("year") Year year) {
        return carModelService.getAllByMakeYear(year);
    }

    /**
     * Get all car models with make year between start and end year.
     * @param startYear Year
     * @param endYear Year
     * @return List of CarModel
     */
    @GetMapping("/years")
    public List<CarModel> getAllCarModelsByYearBetween(@RequestParam Year startYear, @RequestParam Year endYear) {
        return carModelService.getAllByMakeYearBetween(startYear, endYear);
    }

    /**
     * Get all car models of a specific manufacturer
     * @param manufacturer String
     * @return List of CarModel
     */
    @GetMapping("/manufacturer")
    public List<CarModel> getAllCarModelsByManufacturer(@RequestParam String manufacturer) {
        return carModelService.getAllByManufacturer(manufacturer);
    }

    /**
     * Get all car options available to a specific car model
     * @param carModelId Long
     * @return List of Option
     */
    @GetMapping("/{carModelId}/options")
    public List<Option> getAllOptionsByCarModelId(@PathVariable("carModelId") long carModelId) {
        return carModelService.getAllOptionsByCarModelId(carModelId);
    }

    /**
     * Get all cars of a specific car model
     * @param carModelId Long
     * @return List of Car
     */
    @GetMapping("/{carModelId}/cars")
    public List<Car> getAllCarsByCarModelId(@PathVariable("carModelId") long carModelId) {
        return carModelService.getAllCarsByCarModelId(carModelId);
    }

    /**
     * Create a new car model
     * @param carModel CarModel [name, makeYear, manufacturer, image, price]
     * @return CarModel
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CarModel createCarModel(@RequestPart CarModel carModel, @RequestParam("image") MultipartFile image) throws BadRequestException {

        return carModelService.createCarModel(carModel,  image);
    }
}
