package com.booknest.booknest_backend.dto;

import com.booknest.booknest_backend.model.Delivery;

public class DeliveryUpdateRequest {
    private Delivery.DeliveryStatus status;
    private String currentLocation;
    private String message;
    private Double latitude;
    private Double longitude;

    // Getters and setters
    public Delivery.DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(Delivery.DeliveryStatus status) {
        this.status = status;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
