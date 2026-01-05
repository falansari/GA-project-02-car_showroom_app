package com.ga.showroom.repository;

import com.ga.showroom.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    /**
     * Find option by its name
     * @param name String
     * @return Option
     */
    Option findByName(String name);

    /**
     * Find all options belonging to a car model
     * @param carModelId Long
     * @return List of Option
     */
    List<Option> FindAllByCarModelId(Long carModelId);

    /**
     * Find all options belonging to an option category
     * @param optionCategoryId Long
     * @return List of Option
     */
    List<Option> FindAllByOptionCategoryId(Long optionCategoryId);
}
