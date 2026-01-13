package com.ga.showroom.repository;

import com.ga.showroom.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    /**
     * Find option by its name
     * @param name String
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return Optional<Option>
     */
    Optional<Option> findByNameAndCarModelIdAndOptionCategoryId(String name, Long carModelId, Long optionCategoryId);

    /**
     * Find option car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return List<Option>
     */
    List<Option> findByCarModelIdAndOptionCategoryId(Long carModelId, Long optionCategoryId);

    /**
     * Find option car model and option category and option id
     * @param carModelId Long
     * @param optionCategoryId Long
     * @param optionId Long
     * @return Optional<Option>
     */
    Optional<Option> findByCarModelIdAndOptionCategoryIdAndId(Long carModelId, Long optionCategoryId, Long optionId);

}
