package com.booknest.booknest_backend.repository;

import com.booknest.booknest_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders by buyer
    List<Order> findByBuyerId(Long buyerId);

    // Get all orders for a specific book
    List<Order> findByBookId(Long bookId);

    // Get all orders by seller
    List<Order> findByBookSellerId(Long sellerId);
}