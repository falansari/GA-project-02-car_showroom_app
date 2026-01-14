package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.Order;
import com.ga.showroom.repository.OrderRepository;
import com.ga.showroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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
     * Create a new car order
     * @param car Car {vinNumber, registrationNumber, insurancePolicy, modelId, ownerId}
     * @param options List of Option IDs [id, id, id, ...]
     * @return Order
     */
    public Order createOrder(Car car, List<Long> options) {
        Order order = new Order();

        //TODO: Create a base car (model, owner, vin, registration, insurance)
        //TODO: (Loop) Create car's options, as many added in the list:
            //TODO: Check there's only 1 selection per option category. If there's repeat, throw a relevant error.
            //TODO: Check the option exists for the chosen car model, otherwise skip with a message printout
            //TODO: Create CarOption
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
