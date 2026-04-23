package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.dto.DeliveryUpdateRequest;
import com.booknest.booknest_backend.model.Delivery;
import com.booknest.booknest_backend.model.Order;
import com.booknest.booknest_backend.repository.DeliveryRepository;
import com.booknest.booknest_backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, OrderRepository orderRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    // Get delivery by order id
    public Delivery getDelivery(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found!"));
    }

    // Create delivery when order is confirmed
    public Delivery createDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

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

    // Seller update
    public Delivery updateDelivery(Long orderId, String email,
                                   String location, Double lat,
                                   Double lng, String status,
                                   String message) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (!order.getBook().getSeller().getEmail().equals(email)) {
            throw new RuntimeException("Only seller can update delivery!");
        }

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseGet(() -> {
                    Delivery newDelivery = new Delivery();
                    newDelivery.setOrder(order);
                    newDelivery.setStatus(Delivery.DeliveryStatus.PROCESSING);
                    newDelivery.setCurrentLocation(order.getBook().getLocation());
                    newDelivery.setMessage("Delivery created automatically");
                    return deliveryRepository.save(newDelivery);
                });

        delivery.setCurrentLocation(location);
        delivery.setLatitude(lat);
        delivery.setLongitude(lng);
        delivery.setStatus(Delivery.DeliveryStatus.valueOf(status.toUpperCase()));
        delivery.setMessage(message);
        delivery.setUpdatedAt(LocalDateTime.now());

        return deliveryRepository.save(delivery);
    }

    // ✅ ADMIN UPDATE (FIXED HERE 🔥)
    public Delivery updateDeliveryByAdmin(Long orderId, DeliveryUpdateRequest req) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseGet(() -> {
                    Delivery newDelivery = new Delivery();
                    newDelivery.setOrder(order);
                    newDelivery.setStatus(Delivery.DeliveryStatus.PROCESSING);
                    newDelivery.setCurrentLocation(order.getBook().getLocation());
                    newDelivery.setMessage("Delivery created by admin");
                    return deliveryRepository.save(newDelivery);
                });

        if (req.getStatus() != null) {
            try {
                delivery.setStatus(req.getStatus());
            } catch (Exception e) {
                throw new RuntimeException("Invalid delivery status: " + req.getStatus());
            }
        }
        System.out.println("ADMIN UPDATE HIT for order: " + orderId);
        System.out.println("STATUS: " + req.getStatus());
        System.out.println("LOCATION: " + req.getCurrentLocation());
        System.out.println("MESSAGE: " + req.getMessage());
        if (req.getCurrentLocation() != null) {
            delivery.setCurrentLocation(req.getCurrentLocation());
        }
        if (req.getMessage() != null) {
            delivery.setMessage(req.getMessage());
        }
        if (req.getLatitude() != null) {
            delivery.setLatitude(req.getLatitude());
        }
        if (req.getLongitude() != null) {
            delivery.setLongitude(req.getLongitude());
        }

        delivery.setUpdatedAt(LocalDateTime.now());

        delivery = deliveryRepository.save(delivery);

// 🔥 ADD THIS PART (IMPORTANT)
        if (req.getStatus() != null) {
            try {
                if (req.getStatus() == Delivery.DeliveryStatus.DELIVERED) {
                    order.setStatus(Order.OrderStatus.DELIVERED);
                } else {
                    order.setStatus(Order.OrderStatus.CONFIRMED);
                }

                orderRepository.save(order);
                orderRepository.save(order);
            } catch (Exception e) {
                throw new RuntimeException("Invalid order status sync");
            }
        }

        return delivery;
    }
}