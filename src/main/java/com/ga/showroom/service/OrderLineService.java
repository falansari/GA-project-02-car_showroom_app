package com.ga.showroom.service;

import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.Car;
import com.ga.showroom.model.Order;
import com.ga.showroom.model.OrderLine;
import com.ga.showroom.repository.CarRepository;
import com.ga.showroom.repository.OrderLineRepository;
import com.ga.showroom.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineService {
    OrderLineRepository orderLineRepository;
    OrderRepository orderRepository;
    CarRepository carRepository;

    public OrderLineService(OrderLineRepository orderLineRepository, OrderRepository orderRepository,  CarRepository carRepository) {
        this.orderLineRepository = orderLineRepository;
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
    }

    /**
     * Get order line by its ID
     * @param orderLineId Long
     * @return OrderLine
     */
    public OrderLine getOrderLineById(Long orderLineId) {
        return orderLineRepository.findById(orderLineId)
                .orElseThrow(() -> new InformationNotFoundException("Order line with ID " +  orderLineId + " not found"));
    }

    /**
     * Get all order lines belonging to an order
     * @param orderId Long
     * @return List of OrderLine
     */
    public List<OrderLine> getOrderLinesByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId))
            throw new  InformationNotFoundException("Order with ID " + orderId + " not found");

        return orderLineRepository.findAllByOrderId(orderId);
    }

    /**
     * Create new order line belonging to an order
     * @param orderId Long
     * @param carId Long
     * @param price Double
     * @return OrderLine
     */
    public OrderLine createOrderLine(Long orderId, Long carId, Double price) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InformationNotFoundException("Order with ID " + orderId + " not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new InformationNotFoundException("Car with ID " + carId + " not found"));

        OrderLine orderLine = new OrderLine();
        orderLine.setOrder(order);
        orderLine.setCar(car);
        orderLine.setPrice(price);

        return  orderLineRepository.save(orderLine);
    }
}
