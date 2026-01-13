package com.ga.showroom.controller;

import com.ga.showroom.model.Order;
import com.ga.showroom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getById(orderId);
    }

    @GetMapping("/date/{orderDate}")
    public List<Order> getOrdersByOrderDate(@PathVariable("orderDate") LocalDateTime orderDate) {
        return orderService.getByOrderDate(orderDate);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable("customerId") Long customerId) {
        return orderService.getByCustomerId(customerId);
    }

    @GetMapping("/salesman/{salesmanId}")
    public List<Order> getOrdersBySalesmanId(@PathVariable("salesmanId") Long salesmanId) {
        return orderService.getBySalesmanId(salesmanId);
    }

    @GetMapping("/between")
    public List<Order> getOrdersByOrderDateBetween(@RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate) {
        return orderService.getByOrderDateBetween(startDate, endDate);
    }
}
