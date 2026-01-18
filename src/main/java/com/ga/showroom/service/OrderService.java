package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.*;
import com.ga.showroom.repository.OrderRepository;
import com.ga.showroom.repository.UserRepository;
import com.ga.showroom.utility.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

@Service
public class OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    OptionService optionService;
    CarModelService carModelService;
    CarOptionService carOptionService;
    CarService carService;
    Uploads uploads;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        OptionService optionService,
                        CarModelService carModelService,
                        CarOptionService carOptionService,
                        CarService carService,
                        Uploads uploads) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.optionService = optionService;
        this.carModelService = carModelService;
        this.carOptionService = carOptionService;
        this.carService = carService;
        this.uploads = uploads;
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
        List<CarOption> carOptions = new ArrayList<>();
        Double totalPrice = car.getCarModel().getPrice();

        // (Loop) Create car's options, as many added in the list:
        for (Long option : options) {
            Option optionObj = optionService.getOptionById(option);

            if (optionObj == null) {
                System.out.println("No option found with ID " + option + ". Skipping.");
                continue;
            }

            if (!optionObj.getCarModel().equals(car.getCarModel())) { // Check the option exists for the chosen car model, otherwise skip with a message printout
                System.out.println("Option ID " + option + " does not belong to car model " + car.getCarModel().getId() + ". Skipping.");
                continue;
            }

            // Create CarOption
            CarOption carOption = carOptionService.createCarOption(optionObj.getId(), car.getId());
            carOptions.add(carOption);
            totalPrice += carOption.getOption().getPrice();
        }

        //TODO: Generate and set car's image to car (temporarily using stock model image)

        // Create a base car (model, owner, vin, registration, insurance, order)
        Car newCar = carService.createCar(
                car,
                car.getOwner(),
                car.getCarModel(),
                car.getCarModel().getImage(),
                carOptions,
                order);
        // Set car to order
        order.setCar(newCar);
        // Set car's owner as order customer
        order.setCustomer(newCar.getOwner());
        // set logged-in user as order salesman
        order.setSalesman(getCurrentLoggedInUser());
        //calculate total price based on car's base price + car's list of options, and set it to order
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }
}
