package com.ga.showroom.controller;

import com.ga.showroom.model.OptionCategory;
import com.ga.showroom.service.OptionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Option Category API
 */
@RestController
@RequestMapping(path = "api/option-categories")
public class OptionCategoryController {
    private OptionCategoryService optionCategoryService;

    /**
     * Initialize Option Category's service
     * @param optionCategoryService OptionCategoryService
     */
    @Autowired
    public void setOptionCategoryService(OptionCategoryService optionCategoryService) { this.optionCategoryService = optionCategoryService; }

    /**
     * Get an option category by its ID
     * @param optionCategoryId  Long
     * @return OptionCategory
     */
    @GetMapping("/{optionCategoryId}")
    public OptionCategory getOptionCategoryById(@PathVariable("optionCategoryId") Long optionCategoryId) {
        return optionCategoryService.getOptionCategoryById(optionCategoryId);
    }

    /**
     * Get an option category by its name
     * @param optionCategoryName String
     * @return OptionCategory
     */
    @GetMapping("category/{categoryName}")
    public OptionCategory getOptionCategoryByName(@PathVariable("categoryName") String optionCategoryName) {
        return optionCategoryService.getOptionCategoryByName(optionCategoryName);
    }

    /**
     * Get a list of all option categories
     * @return List of OptionCategory
     */
    @GetMapping("")
    public List<OptionCategory> getAllOptionCategories() {
        return optionCategoryService.getOptionCategories();
    }

    /**
     * Create a new option category
     * @param optionCategory OptionCategory
     * @return OptionCategory
     */
    @PostMapping("")
    public OptionCategory createOptionCategory(@RequestBody OptionCategory optionCategory) {
        return optionCategoryService.createOptionCategory(optionCategory);
    }

    /**
     * Update an option category. Allows partial update.
     * @param optionCategoryId Long
     * @param optionCategory OptionCategory
     * @return OptionCategory
     */
    @PatchMapping("/{optionCategoryId}")
    public OptionCategory updateOptionCategory(@PathVariable("optionCategoryId") Long optionCategoryId, @RequestBody OptionCategory optionCategory) {
        return optionCategoryService.updateOptionCategory(optionCategoryId, optionCategory);
    }

    /**
     * Delete an existing option category by its ID
     * @param optionCategoryId Long
     */
    @DeleteMapping("{optionCategoryId}")
    public void deleteOptionCategory(@PathVariable("optionCategoryId") Long optionCategoryId) {
        optionCategoryService.deleteOptionCategory(optionCategoryId);
    }
}
