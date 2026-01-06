package com.ga.showroom.repository;

import com.ga.showroom.model.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    /**
     * Find an option category by name
     * @param name String
     * @return OptionCategory
     */
    OptionCategory findByName(String name);

    /**
     * Find all option categories sorted by name ascending
     * @return List of OptionCategory
     */
    List<OptionCategory> findAllByOrderByNameAsc();


    /**
     * Find all option categories sorted by name descending
     * @return List of OptionCategory
     */
    List<OptionCategory> findAllByOrderByNameDesc();
}
