package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.Delivery;
import com.booknest.booknest_backend.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    // Get delivery by order id
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getDelivery(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(deliveryService.getDelivery(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Create delivery
    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createDelivery(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(deliveryService.createDelivery(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update delivery
    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateDelivery(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String location,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam String status,
            @RequestParam String message) {
        try {
            Delivery delivery = deliveryService.updateDelivery(
                    orderId, userDetails.getUsername(),
                    location, lat, lng, status, message);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}