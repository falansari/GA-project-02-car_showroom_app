package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.*;
import com.ga.showroom.repository.OrderRepository;
import com.ga.showroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    OptionService optionService;
    CarModelService carModelService;
    CarOptionService carOptionService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        OptionService optionService, CarModelService carModelService,  CarOptionService carOptionService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.optionService = optionService;
        this.carModelService = carModelService;
        this.carOptionService = carOptionService;
    }

    /**
     * Get order by its ID
     * @param orderId Long
     * @return Order
     */
    public Order getById(Long orderId) {
        // TODO: role management
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InformationNotFoundException("Order " + orderId + " not found"));
    }

    /**
     * Get all orders. Gets all in database if admin, otherwise user's own orders only.
     * @return List of Order
     */
    public List<Order> getAll() {
        // TODO: role management
        return orderRepository.findAll();
    }

    /**
     * Get all orders conducted on a certain date. Gets all in database if admin, otherwise user's own orders only.
     * @param orderDate LocalDateTime
     * @return List of Order
     */
    public List<Order> getByOrderDate(LocalDateTime orderDate) {
        // TODO: role management
        return orderRepository.findByCreatedAt(orderDate);
    }

    /**
     * Get all orders by their associated customer ID
     * @param customerId Long
     * @return List of Order
     */
    public List<Order> getByCustomerId(Long customerId) {
        // TODO: role management
        if (!userRepository.existsById(customerId))
            throw new InformationNotFoundException("Customer with ID " + customerId + " not found");

        return orderRepository.findAllByCustomerId(customerId);
    }

    /**
     * Get all orders by their associated salesman ID
     * @param salesmanId Long
     * @return List of Order
     */
    public List<Order> getBySalesmanId(Long salesmanId) {
        // TODO: role management
        if (!userRepository.existsById(salesmanId))
            throw new InformationNotFoundException("salesman with ID " + salesmanId + " not found");

        return orderRepository.findAllBySalesmanId(salesmanId);
    }

    /**
     * Get all orders conducted between start and end date. Admin sees all orders, normal user sees their own orders only.
     * @param orderDateStart LocalDateTime
     * @param orderDateEnd LocalDateTime
     * @return List of Order
     */
    public List<Order> getByOrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd) {
        // TODO: role management
        return orderRepository.findByCreatedAtBetween(orderDateStart, orderDateEnd);
    }

    /**
     * Create a new car order. The associated salesman is the logged-in user.
     * @param car Car {vinNumber, registrationNumber, insurancePolicy, modelId, ownerId}
     * @param options List of Option IDs [id, id, id, ...]
     * @return Order
     */
    public Order createOrder(Car car, List<Long> options) {
        Order order = new Order();
        CarModel carModel = car.getCarModel();
        List<CarOption> carOptions = new ArrayList<>();

        // (Loop) Create car's options, as many added in the list:
        for (Long option : options) {
            Option optionObj = optionService.getOptionById(option);

            if (optionObj == null) {
                System.out.println("No option found with ID " + option + ". Skipping.");
                continue;
            }

            if (!optionObj.getCarModel().equals(carModel)) { // Check the option exists for the chosen car model, otherwise skip with a message printout
                System.out.println("Option ID " + option + " does not belong to car model " + carModel.getId() + ". Skipping.");
                continue;
            }

            // Create CarOption
            CarOption carOption = carOptionService.createCarOption(optionObj.getId(), car.getId());
            carOptions.add(carOption);
        }

        //TODO: Create a base car (model, owner, vin, registration, insurance, order)
        //TODO: Generate and set car's image to car (temporarily using stock model image)
        //TODO: Set car to order
        //TODO: Set car's owner as order customer
        //TODO: set logged in user as order salesman
        //TODO: calculate total price based on car's base price + car's list of options, and set it to order
        //TODO: update car
        //TODO: save order

        return orderRepository.save(order);
    }
}
