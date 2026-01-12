package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.OptionCategory;
import com.ga.showroom.repository.OptionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

@Service
public class OptionCategoryService {
    OptionCategoryRepository optionCategoryRepository;

    @Autowired
    public void setOptionCategoryRepository(OptionCategoryRepository optionCategoryRepository) {
        this.optionCategoryRepository = optionCategoryRepository;
    }

    /**
     * Get an option category by its ID
     * @param optionCategoryId Long
     * @return OptionCategory
     */
    public OptionCategory getOptionCategoryById(Long optionCategoryId) {
        return optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new InformationNotFoundException("OptionCategory with id " + optionCategoryId + " not found"));
    }

    /**
     * Get an option category by its name
     * @param optionCategoryName String
     * @return OptionCategory
     */
    public OptionCategory getOptionCategoryByName(String optionCategoryName) {
        OptionCategory optionCategory = optionCategoryRepository.findByName(optionCategoryName);

        if (optionCategory == null) throw new InformationNotFoundException("OptionCategory with name " + optionCategoryName + " not found");
        return optionCategory;
    }

    /**
     * Get a list of all option categories
     * @return List of OptionCategory
     */
    public List<OptionCategory> getOptionCategories() {
        return optionCategoryRepository.findAll();
    }

    /**
     * Create a new option category. Only admin allowed.
     * @param optionCategory OptionCategory
     * @return OptionCategory
     */
    public OptionCategory createOptionCategory(OptionCategory optionCategory) {
        System.out.println("User role: " + getCurrentLoggedInUser().getRole());
        // TODO: admin only should be able to create a option category. add with role management issue.
        /*if (!Objects.equals(getCurrentLoggedInUser().getRole(), "admin")) {
            throw new AccessDeniedException("Only an admin can access OptionCategory");
        }*/

        OptionCategory existingOptionCategory = optionCategoryRepository.findByName(optionCategory.getName());

        if (existingOptionCategory != null) throw new InformationExistException("Option Category with name " + optionCategory.getName()+ " already exists");

        return optionCategoryRepository.save(optionCategory);
    }

    /**
     * Update an existing option category. Partial update allowed. Admin only.
     * @param optionCategoryId Long
     * @param optionCategory OptionCategory
     * @return OptionCategory
     */
    public OptionCategory updateOptionCategory(Long optionCategoryId, OptionCategory optionCategory) {
        // TODO: admin only should be able to create a option category. add with role management issue.
        /*if (!Objects.equals(getCurrentLoggedInUser().getRole(), "admin")) {
            throw new AccessDeniedException("Only an admin can access OptionCategory");
        }*/

        OptionCategory storedOptionCategory = getOptionCategoryById(optionCategoryId);

        if (storedOptionCategory == null) throw new InformationNotFoundException("OptionCategory with id " + optionCategoryId + " not found");

        if (optionCategory.getName() != null) {
            OptionCategory existingOptionCategory = optionCategoryRepository.findByName(optionCategory.getName());

            if (existingOptionCategory != null) throw new InformationExistException("Option Category with name " + optionCategory.getName()+ " already exists");

            storedOptionCategory.setName(optionCategory.getName());
        }

        return optionCategoryRepository.save(storedOptionCategory);
    }

    /**
     * Delete an existing option category by its ID. Admin only.
     * @param optionCategoryId Long
     */
    public void deleteOptionCategory(Long optionCategoryId) {
        // TODO: admin only should be able to create a option category. add with role management issue.
        /*if (!Objects.equals(getCurrentLoggedInUser().getRole(), "admin")) {
            throw new AccessDeniedException("Only an admin can access OptionCategory");
        }*/

        OptionCategory storedOptionCategory = getOptionCategoryById(optionCategoryId);

        if (storedOptionCategory == null) throw new InformationNotFoundException("OptionCategory with id " + optionCategoryId + " not found");

        optionCategoryRepository.delete(storedOptionCategory);
    }
}
