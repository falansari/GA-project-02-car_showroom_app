package com.ga.showroom.repository;

import com.ga.showroom.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository  extends JpaRepository<OrderLine, Long> {
    /**
     * Find all order lines by their order id
     * @param orderId Long
     * @return List of OrderLine
     */
    List<OrderLine> findAllByOrderId(Long orderId);
}
