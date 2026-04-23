package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Get analytics
    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalytics() {
        try {
            Map<String, Object> analytics = adminService.getAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(adminService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok("User deleted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all books
    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks() {
        try {
            return ResponseEntity.ok(adminService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete book
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            adminService.deleteBook(id);
            return ResponseEntity.ok("Book deleted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/geo")
    public ResponseEntity<?> getCoordinates(@RequestParam String place) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q="
                    + place + "&format=json&limit=1";

            RestTemplate restTemplate = new RestTemplate();

            // 🔥 REQUIRED HEADERS
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "booknest-app");   // VERY IMPORTANT

            org.springframework.http.HttpEntity<String> entity =
                    new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> response =
                    restTemplate.exchange(url,
                            org.springframework.http.HttpMethod.GET,
                            entity,
                            String.class);

            // convert JSON manually
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            java.util.List list =
                    mapper.readValue(response.getBody(), java.util.List.class);

            if (list == null || list.isEmpty()) {
                return ResponseEntity.badRequest().body("Location not found");
            }

            java.util.Map result = (java.util.Map) list.get(0);

            double lat = Double.parseDouble(result.get("lat").toString());
            double lon = Double.parseDouble(result.get("lon").toString());

            java.util.Map<String, Double> res = new java.util.HashMap<>();
            res.put("latitude", lat);
            res.put("longitude", lon);

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 IMPORTANT for debugging
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // Get all orders
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(adminService.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}