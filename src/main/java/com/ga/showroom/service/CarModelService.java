package com.ga.showroom.service;

import com.ga.showroom.exception.AccessDeniedException;
import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.model.enums.Role;
import com.ga.showroom.repository.CarModelRepository;
import com.ga.showroom.utility.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.Year;
import java.util.List;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

@Service
public class CarModelService {
    CarModelRepository carModelRepository;
    Uploads uploads;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository, Uploads uploads) {
        this.carModelRepository = carModelRepository;
        this.uploads = uploads;
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
    public List<Option> getAllOptionsByCarModelId(Long carModelId) {
        return carModelRepository.findAllOptionsById(carModelId);
    }

    /**
     * Find all cars with of a car model
     * @param carModelId Long
     * @return List of Car
     */
    public List<Car> getAllCarsByCarModelId(Long carModelId) {
        return carModelRepository.findAllCarsById(carModelId);
    }

    /**
     * Create a new car model
     * @param carModel CarModel
     * @param image MultipartFile [PNG, JPEG, JPG]
     * @return CarModel
     */
    public CarModel createCarModel(CarModel carModel, MultipartFile image) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER))
            throw new AccessDeniedException("You are not allowed to create a car model");

        CarModel existingCarModel = carModelRepository.findByNameAndMakeYear(carModel.getName(), carModel.getMakeYear());

        if (existingCarModel != null)
            throw new InformationExistException("Car model with name " + carModel.getName() + " and year " + carModel.getMakeYear() + " already exists");

        String uploadedImage = uploads.uploadImage("uploads/model-images", image);

        if (uploadedImage != null) carModel.setImage(uploadedImage);

        return carModelRepository.save(carModel);
    }

    /**
     * Update an existing car model
     *
     * @param carModelId Long
     * @param carModel   CarModel
     * @param image      MultipartFile [PNG, JPEG, JPG]
     * @return CarModel
     */
    public CarModel updateCarModel(Long carModelId, CarModel carModel, MultipartFile image) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER))
            throw new AccessDeniedException("You are not allowed to update a car model");

        CarModel updatedCarModel = getCarModelById(carModelId);

        if (updatedCarModel == null) throw new InformationNotFoundException("Car Model with ID " + carModel.getId() + " not found");

        CarModel existingCarModel = carModelRepository.findByName(carModel.getName());

        if (existingCarModel != null && updatedCarModel != existingCarModel)
            throw new InformationExistException("Car model with name " + carModel.getName() + " already exists");

        String uploadedImage = uploads.uploadImage("uploads/model-images", image);

        if (uploadedImage != null) {
            uploads.deleteImage(Paths.get("uploads/model-images", updatedCarModel.getImage()));

            updatedCarModel.setImage(uploadedImage);
        }

        if (carModel.getName() != null) updatedCarModel.setName(carModel.getName());
        if (carModel.getMakeYear() != null) updatedCarModel.setMakeYear(carModel.getMakeYear());
        if (carModel.getManufacturer() != null) updatedCarModel.setManufacturer(carModel.getManufacturer());
        if (carModel.getPrice() != null) updatedCarModel.setPrice(carModel.getPrice());

        return carModelRepository.save(updatedCarModel);
    }

    /**
     * Delete existing car model
     * @param carModelId Long
     */
    public void deleteCarModel(Long carModelId) {
        if (getCurrentLoggedInUser().getRole().equals(Role.CUSTOMER))
            throw new AccessDeniedException("You are not allowed to delete a car model");

        CarModel carModel = getCarModelById(carModelId);

        if (carModel == null) throw new InformationNotFoundException("Car Model with ID " + carModelId + " not found");

        carModelRepository.delete(carModel);
    }
}
