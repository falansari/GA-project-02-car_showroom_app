package com.ga.showroom.repository;

import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    OptionRepository getOptionRepository();
    /**
     * Find a car model by its name
     * @param name String
     * @return CarModel
     */
    CarModel findByName(String name);

    /**
     * Find all car models made in a specific year
     * @param makeYear Year
     * @return List of CarModel
     */
    List<CarModel> findAllByMakeYear(Year makeYear);

    /**
     * Find all car models created between start and end year.
     * @param start Year
     * @param end Year
     * @return List of CarModel
     */
    List<CarModel> findAllByMakeYearBetween(Year start, Year end);

    /**
     * Find all car models by their manufacturer name
     * @param manufacturer String
     * @return List of CarModel
     */
    List<CarModel> findAllByManufacturer(String manufacturer);

    /**
     * Find all options belonging to a car model
     * @param carModelId Long
     * @return List of Option
     */
    List<Option> findAllOptionsByCarModelId(Long carModelId);
}
