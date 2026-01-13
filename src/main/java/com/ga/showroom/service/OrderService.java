package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
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
        return orderRepository.findByOrderDate(orderDate);
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
        return orderRepository.findByOrderDateBetween(orderDateStart, orderDateEnd);
    }
}
