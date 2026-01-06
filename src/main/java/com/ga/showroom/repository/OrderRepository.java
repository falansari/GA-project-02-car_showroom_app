package com.ga.showroom.repository;

import com.ga.showroom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Find an order by its datetime.
     * @param localDateTime LocalDateTime
     * @return Order
     */
    Order findByOrderDate(LocalDateTime localDateTime);

    /**
     * Find all orders belonging to a customer
     * @param customerId Long
     * @return List of Order
     */
    List<Order> findAllByCustomerId(Long customerId);

    /**
     * Find all orders belonging to a salesman
     * @param salesmanId Long
     * @return List of Order
     */
    List<Order> findAllBySalesmanId(Long salesmanId);

    /**
     * Find all orders sorted ascending by total price
     * @return List of Order
     */
    List<Order> findAllByTotalPrice();

    /**
     * Find all orders between start and end date.
     * @param localDateTimeStart LocalDateTime
     * @param localDateTimeEnd LocalDateTime
     * @return List of Order
     */
    List<Order> findByOrderDateBetween(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}