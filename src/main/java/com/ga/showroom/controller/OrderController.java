package com.ga.showroom.controller;

import com.ga.showroom.model.Car;
import com.ga.showroom.model.Order;
import com.ga.showroom.model.request.CreateOrderRequest;
import com.ga.showroom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Order API
 */
@RestController
@RequestMapping(path = "api/orders")
public class OrderController {
    /**
     * Initialize Order Service
     */
    @Autowired
    private OrderService orderService;

    /**
     * Get all orders in storage
     * @return List of Order
     */
    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }

    /**
     * Get order's info by its ID
     * @param orderId Long
     * @return Order
     */
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getById(orderId);
    }

    /**
     * Get all orders done on a specific date
     * @param orderDate LocalDateTime
     * @return List of Order
     */
    @GetMapping("/date/{orderDate}")
    public List<Order> getOrdersByOrderDate(@PathVariable("orderDate") LocalDateTime orderDate) {
        return orderService.getByOrderDate(orderDate);
    }

    /**
     * Get all orders belonging to a specific customer
     * @param customerId Long
     * @return List of Order
     */
    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable("customerId") Long customerId) {
        return orderService.getByCustomerId(customerId);
    }

    /**
     * Get all orders conducted by a specific salesman
     * @param salesmanId Long
     * @return List of Order
     */
    @GetMapping("/salesman/{salesmanId}")
    public List<Order> getOrdersBySalesmanId(@PathVariable("salesmanId") Long salesmanId) {
        return orderService.getBySalesmanId(salesmanId);
    }

    /**
     * Get all orders conducted between start and end date.
     * @param startDate LocalDateTime
     * @param endDate LocalDateTime
     * @return List of Order
     */
    @GetMapping("/between")
    public List<Order> getOrdersByOrderDateBetween(@RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate) {
        return orderService.getByOrderDateBetween(startDate, endDate);
    }

    /**
     * Create a new car order
     * @param request CreateOrderRequest [Car, modelId, ownerId, List of options]
     * @return Order
     */
    @PostMapping("")
    public Order createOrder(@RequestBody CreateOrderRequest request) {
            Car car = request.getCar();
            Long modelId = request.getModelId();
            Long ownerId = request.getOwnerId();
            List<Long> optionIds = request.getOptions();

            return orderService.createOrder(car, modelId, ownerId,optionIds);
    }
}
