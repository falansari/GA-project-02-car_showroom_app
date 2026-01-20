package com.ga.showroom.repository;

import com.ga.showroom.model.CarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarOptionRepository extends JpaRepository<CarOption, Long> {
    /**
     * Find car option by its name and car it belongs to
     * @param optionId Long
     * @param carId Long
     * @return CarOption
     */
    CarOption findByOptionIdAndCarId(Long optionId, Long carId);

    /**
     * Find all car options belonging to a specific option
     * @param optionId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByOptionId(Long optionId);

    /**
     * Find all car options belonging to a specific car
     * @param carId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByCarId(Long carId);

    /**
     * Find all car options belonging to vehicles owned by owner ID
     * @param carOwnerId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByCarOwnerId(Long carOwnerId);

    /**
     * Find all car options belonging to a specific option for a specific customer's cars
     * @param optionId Long
     * @param carOwnerId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByOptionIdAndCarOwnerId(Long optionId, Long carOwnerId);
}