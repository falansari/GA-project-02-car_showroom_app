package com.ga.showroom.controller;

import com.ga.showroom.model.Option;
import com.ga.showroom.model.OptionCategory;
import com.ga.showroom.model.User;
import com.ga.showroom.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/")
public class OptionController {
    private OptionService optionService;

    @Autowired
    public void setOptionService(OptionService optionService){
        this.optionService = optionService;
    }

    /**
     * read all options related to specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return List<Option>
     */
    @GetMapping(path = "car-models/{carModelId}/option-categories/{optionCategoryId}/options")
    public List<Option> getOptions(@PathVariable("carModelId") Long carModelId, @PathVariable("optionCategoryId") Long optionCategoryId){
        return optionService.getOptions(carModelId, optionCategoryId);
    }

    /**
     * Read option by its ID
     * @param optionId Long
     * @return Option
     */
    @GetMapping(path = "options/{optionId}")
    public  Option getOptionById(@PathVariable("optionId") Long optionId){
        return optionService.getOptionById(optionId);
    }

    /**
     * read one options related to specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @param optionId Long
     * @return Option
     */
    @GetMapping(path = "car-models/{carModelId}/option-categories/{optionCategoryId}/options/{optionId}")
    public Option getOption(@PathVariable("carModelId") Long carModelId, @PathVariable("optionCategoryId") Long optionCategoryId, @PathVariable("optionId") Long optionId){
        return optionService.getOption(carModelId, optionCategoryId, optionId);
    }

    /**
     * create one options related for specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return Option
     */
    @PostMapping(path = "car-models/{carModelId}/option-categories/{optionCategoryId}/options")
    public Option createOption(@PathVariable("carModelId") Long carModelId, @PathVariable("optionCategoryId") Long optionCategoryId, @RequestBody Option option){
        return optionService.createOption(carModelId, optionCategoryId, option);
    }

    /**
     * uodate one options related for specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @param optionId Long
     * @Param option Option
     * @return Option
     */
    @PutMapping(path = "car-models/{carModelId}/option-categories/{optionCategoryId}/options/{optionId}")
    public Option updateOption(@PathVariable("carModelId") Long carModelId, @PathVariable("optionCategoryId") Long optionCategoryId,
                               @PathVariable("optionId") Long optionId, @RequestBody Option option){
        return optionService.updateOption(carModelId, optionCategoryId, optionId, option);
    }



}
