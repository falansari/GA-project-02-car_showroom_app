package com.ga.showroom.repository;

import com.ga.showroom.model.CarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarOptionRepository extends JpaRepository<CarOption, Long> {
    /**
     * Find car option by its name and car it belongs to
     * @param optionName String
     * @param carId Long
     * @return CarOption
     */
    CarOption findByOptionNameAndCarId(String optionName, Long carId);

    /**
     * Find all car options belonging to a specific option
     * @param optionId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByOptionId(Long optionId);

    /**
     * Find all options belonging to a specific option
     * @param optionName String
     * @return List of CarOption
     */
    List<CarOption> findAllByOptionName(String optionName);

    /**
     * Find all car options belonging to a specific car
     * @param carId Long
     * @return List of CarOption
     */
    List<CarOption> findAllByCarId(Long carId);
}
