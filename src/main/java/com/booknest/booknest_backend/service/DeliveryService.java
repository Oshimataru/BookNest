package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Delivery;
import com.booknest.booknest_backend.model.Order;
import com.booknest.booknest_backend.repository.DeliveryRepository;
import com.booknest.booknest_backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Get delivery by order id
    public Delivery getDelivery(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found!"));
    }

    // Create delivery when order is confirmed
    public Delivery createDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        // Check if delivery already exists
        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            return deliveryRepository.findByOrderId(orderId).get();
        }

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setCurrentLocation(order.getBook().getLocation());
        delivery.setStatus(Delivery.DeliveryStatus.PROCESSING);
        delivery.setMessage("Order is being processed!");

        return deliveryRepository.save(delivery);
    }

    // Update delivery location and status
    public Delivery updateDelivery(Long orderId, String email,
                                   String location, Double lat,
                                   Double lng, String status,
                                   String message) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        // Only seller can update delivery
        if (!order.getBook().getSeller().getEmail().equals(email)) {
            throw new RuntimeException("Only seller can update delivery!");
        }

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found!"));

        delivery.setCurrentLocation(location);
        delivery.setLatitude(lat);
        delivery.setLongitude(lng);
        delivery.setStatus(Delivery.DeliveryStatus.valueOf(status.toUpperCase()));
        delivery.setMessage(message);
        delivery.setUpdatedAt(LocalDateTime.now());

        return deliveryRepository.save(delivery);
    }
}