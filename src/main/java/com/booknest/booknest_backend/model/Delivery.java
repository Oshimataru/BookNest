package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String currentLocation;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PROCESSING;

    private String message;

    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum DeliveryStatus {
        PROCESSING, SHIPPED, OUT_FOR_DELIVERY, DELIVERED
    }

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public String getCurrentLocation() { return currentLocation; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public DeliveryStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}