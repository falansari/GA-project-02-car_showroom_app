package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.repository.CarModelRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
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
     * @param image MultipartFile [PNG, JPEG]
     * @return CarModel
     */
    public CarModel createCarModel(CarModel carModel, MultipartFile image) throws BadRequestException {
        CarModel existingCarModel = carModelRepository.findByName(carModel.getName());

        if (existingCarModel != null) throw new InformationExistException("Car model with name " + carModel.getName() + " already exists");

        // handle image upload
        if (!image.isEmpty()) {
            String fileType = image.getContentType();

            if (!Objects.equals(fileType, "image/jpeg")
                    && !Objects.equals(fileType, "image/jpg")
                    && !Objects.equals(fileType, "image/png")) {
                throw new BadRequestException("Invalid file type");
            }
            try {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                // better upload location (outside classpath)
                Path uploadPath = Paths.get("uploads/model-images");
                Files.createDirectories(uploadPath);

                Files.copy(
                        image.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                // save filename in DB
                carModel.setImage(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return carModelRepository.save(carModel);
    }

    /**
     * Update an existing car model
     *
     * @param carModelId Long
     * @param carModel   CarModel
     * @param image      MultipartFile [PNG, JPEG, JPG]
     * @return CarModel
     * @throws BadRequestException Bad Request handling for file upload
     */
    public CarModel updateCarModel(Long carModelId, CarModel carModel, MultipartFile image) throws BadRequestException {
        CarModel updatedCarModel = getCarModelById(carModelId);

        if (updatedCarModel == null) throw new InformationNotFoundException("Car Model with ID " + carModel.getId() + " not found");

        CarModel existingCarModel = carModelRepository.findByName(carModel.getName());

        if (existingCarModel != null && updatedCarModel != existingCarModel)
            throw new InformationExistException("Car model with name " + carModel.getName() + " already exists");

        // handle image upload
        if (!image.isEmpty()) {
            String fileType = image.getContentType();

            if (!Objects.equals(fileType, "image/jpeg")
                    && !Objects.equals(fileType, "image/jpg")
                    && !Objects.equals(fileType, "image/png")) {
                throw new BadRequestException("Invalid file type");
            }
            try {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                // better upload location (outside classpath)
                Path uploadPath = Paths.get("uploads/model-images");

                // Delete previous image from storage
                Path oldFilePath = uploadPath.resolve(updatedCarModel.getImage());
                if (Files.exists(oldFilePath)) Files.delete(oldFilePath);

                Files.createDirectories(uploadPath);

                Files.copy(
                        image.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                // save filename in DB
                updatedCarModel.setImage(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        if (carModel.getName() != null) updatedCarModel.setName(carModel.getName());
        if (carModel.getMakeYear() != null) updatedCarModel.setMakeYear(carModel.getMakeYear());
        if (carModel.getManufacturer() != null) updatedCarModel.setManufacturer(carModel.getManufacturer());
        if (carModel.getPrice() != null) updatedCarModel.setPrice(carModel.getPrice());

        return carModelRepository.save(updatedCarModel);
    }
}
