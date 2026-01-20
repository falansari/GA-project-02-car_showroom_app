package com.ga.showroom.controller;

import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Year;
import java.util.List;

/**
 * Car Model API
 */
@RestController
@RequestMapping(path = "api/car-models")
public class CarModelController {
    private CarModelService carModelService;

    /**
     * Initialize Car model service
     * @param carModelService CarModelService
     */
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
    public CarModel createCarModel(@RequestPart("carModel") CarModel carModel, @RequestParam("image") MultipartFile image) {
        return carModelService.createCarModel(carModel, image);
    }

    /**
     * Update existing car model
     * @param carModelId Long
     * @param carModel CarModel [name, makeYear, manufacturer, image, price]
     * @param image MultipartFile [PNG, JPEG, JPG]
     * @return CarModel
     */
    @PatchMapping(value = "/{carModelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CarModel updateCarModel(@PathVariable("carModelId") Long carModelId, @RequestPart("carModel") CarModel carModel, @RequestParam("image") MultipartFile image) {
        return carModelService.updateCarModel(carModelId, carModel,  image);
    }

    /**
     * Delete existing car model
     * @param carModelId Long
     */
    @DeleteMapping("/{carModelId}")
    public void deleteCarModel(@PathVariable("carModelId") Long carModelId) {
        carModelService.deleteCarModel(carModelId);
    }

    /**
     * Download stored car model's image
     * @param carModelId Long
     * @return ResponseEntity Resource The image
     */
    @GetMapping("/image/{carModelId}")
    public ResponseEntity<Resource> getImage(@PathVariable("carModelId") Long carModelId) {
        return carModelService.downloadCarModelImage(carModelId);
    }
}
