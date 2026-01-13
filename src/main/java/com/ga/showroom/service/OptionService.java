package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.CarModel;
import com.ga.showroom.model.Option;
import com.ga.showroom.model.OptionCategory;
import com.ga.showroom.repository.CarModelRepository;
import com.ga.showroom.repository.OptionCategoryRepository;
import com.ga.showroom.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionService {
    private OptionRepository optionRepository;
    private CarModelRepository carModelRepository;
    private OptionCategoryRepository optionCategoryRepository;
    private UserService userService;

    @Autowired
    public void setOptionRepository(OptionRepository optionRepository, CarModelRepository carModelRepository,
                                    OptionCategoryRepository optionCategoryRepository, UserService userService){
        this.optionRepository = optionRepository;
        this.carModelRepository = carModelRepository;
        this.optionCategoryRepository = optionCategoryRepository;
        this.userService =userService;
    }

    /**
     * read all options related to specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return List<Option>
     */
    public List<Option> getOptions(Long carModelId, Long optionCategoryId){
        List<Option> options = optionRepository.findByCarModelIdAndOptionCategoryId(carModelId, optionCategoryId);
        if(options.isEmpty()){
            throw new InformationNotFoundException("No Options for car model " + carModelId + " and for option category " + optionCategoryId);
        }else{
            return options;
        }
    }

    /**
     * Find option by its ID
     * @param optionId Long
     * @return Option
     */
    public Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new InformationNotFoundException("Option with id " + optionId + " not found"));
    }

    /**
     * read one options related to specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @param optionId Long
     * @return Option
     */
    public Option getOption(Long carModelId, Long optionCategoryId, Long optionId){
        return optionRepository.findByCarModelIdAndOptionCategoryIdAndId(carModelId, optionCategoryId, optionId)
                .orElseThrow(() -> new InformationNotFoundException("Option " + optionId + " is not found for car model " + carModelId + " and for option category " + optionCategoryId));
    }

    /**
     * create one options related for specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @return Option
     */
    public Option createOption(Long carModelId, Long optionCategoryId, Option optionObj){
        // TODO: admin only should be able to create a option category. add with role management issue.
        /*if (!Objects.equals(userService.getCurrentLoggedInUser().getRole(), "admin")) {
            throw new AccessDeniedException("Only an admin can access OptionCategory");
        }*/
        CarModel carModel = carModelRepository.findById(carModelId).orElseThrow(() -> new InformationNotFoundException("car model " + carModelId + "is not found"));
        OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId).orElseThrow(() -> new InformationNotFoundException("option category " + optionCategoryId + "is not found"));
        Optional<Option> option = optionRepository.findByNameAndCarModelIdAndOptionCategoryId(optionObj.getName(), carModelId, optionCategoryId);

        if(option.isPresent()){
            throw new InformationExistException("The Option " + optionObj.getName() + " already exist for car model " + carModelId + " and for option category " + optionCategoryId);
        }else{
            optionObj.setCarModel(carModel);
            optionObj.setOptionCategory(optionCategory);
            return optionRepository.save(optionObj);
        }
    }
    /**
     * update one options related for specific car model and option category
     * @param carModelId Long
     * @param optionCategoryId Long
     * @param optionId Long
     * @Param option Option
     * @return Option
     */
    public Option updateOption(Long carModelId, Long optionCategoryId, Long optionId, Option optionObj){
        // TODO: admin only should be able to create a option category. add with role management issue.
        /*if (!Objects.equals(userService.getCurrentLoggedInUser().getRole(), "admin")) {
            throw new AccessDeniedException("Only an admin can access OptionCategory");
        }*/

        Option option = optionRepository.findByCarModelIdAndOptionCategoryIdAndId(carModelId, optionCategoryId, optionId)
                .orElseThrow(() -> new InformationNotFoundException("Option " + optionId + " is not found for car model " + carModelId + " and for option category " + optionCategoryId));
        option.setName(optionObj.getName());
        option.setPrice(optionObj.getPrice());

        return optionRepository.save(option);
    }
}
