package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.ExchangeRequest;
import com.booknest.booknest_backend.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "*")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    // Send exchange request
    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long requestedBookId,
            @RequestParam Long offeredBookId,
            @RequestParam(required = false) String message) {
        try {
            ExchangeRequest request = exchangeService.sendRequest(
                    userDetails.getUsername(),
                    requestedBookId, offeredBookId, message);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get my sent requests
    @GetMapping("/sent")
    public ResponseEntity<?> getSentRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ExchangeRequest> requests = exchangeService
                    .getMySentRequests(userDetails.getUsername());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get received requests
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ExchangeRequest> requests = exchangeService
                    .getMyReceivedRequests(userDetails.getUsername());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Accept request
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(exchangeService.acceptRequest(
                    id, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reject request
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(exchangeService.rejectRequest(
                    id, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}