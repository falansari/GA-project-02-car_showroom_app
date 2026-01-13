package com.ga.showroom.controller;

import com.ga.showroom.model.OrderLine;
import com.ga.showroom.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/orderlines")
public class OrderLineController {
    @Autowired
    private OrderLineService orderLineService;

    /**
     * Get all order lines belonging to an order
     * @param orderLineId Long
     * @see GetMapping api/orderlines/{orderId}
     * @return List of OrderLine
     */
    @GetMapping("/{orderLineId}")
    public List<OrderLine> getOrderLinesByOrderId(@PathVariable("orderLineId") Long orderLineId) {
        return  orderLineService.getOrderLinesByOrderId(orderLineId);
    }

    /**
     * Get order line by its ID
     * @param orderLineId Long
     * @see GetMapping api/orderlines/order/{orderLineId}
     * @return OrderLine
     */
    @GetMapping("/order/{orderLineId}")
    public OrderLine getOrderLineById(@PathVariable("orderLineId") Long orderLineId) {
        return orderLineService.getOrderLineById(orderLineId);
    }

    /**
     * Create a new order line belonging to an order with an associated car.
     * The price should be the full price of the vehicle including its paid options if any.
     * @param orderId Long
     * @param carId Long
     * @param price Double
     * @return OrderLine
     */
    @PostMapping("")
    public OrderLine createOrderLine(@RequestParam("orderId") Long orderId, @RequestParam("carId") Long carId, @RequestParam("price") Double price) {
        return orderLineService.createOrderLine(orderId, carId, price);
    }
}
