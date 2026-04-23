package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.dto.DeliveryUpdateRequest;
import com.booknest.booknest_backend.service.DeliveryService;
import com.booknest.booknest_backend.model.Delivery;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin; // Import this

@RestController
@RequestMapping("/api/admin/deliveries")
@CrossOrigin(origins = "http://localhost:5173") // Add this line to allow requests from your React app
public class DeliveryAdminController {

    private final DeliveryService deliveryService;

    public DeliveryAdminController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    // You can also add it to the specific method instead:
    // @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updateDelivery(
            @PathVariable Long orderId,
            @RequestBody DeliveryUpdateRequest req) {
        try {
            Delivery updated = deliveryService.updateDeliveryByAdmin(orderId, req);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
