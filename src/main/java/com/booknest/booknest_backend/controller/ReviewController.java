package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.Review;
import com.booknest.booknest_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Add review
    @PostMapping("/add")
    public ResponseEntity<?> addReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long bookId,
            @RequestParam int rating,
            @RequestParam String comment) {
        try {
            Review review = reviewService.addReview(
                    userDetails.getUsername(), bookId, rating, comment);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get book reviews
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookReviews(@PathVariable Long bookId) {
        try {
            List<Review> reviews = reviewService.getBookReviews(bookId);
            Double avg = reviewService.getAverageRating(bookId);
            return ResponseEntity.ok(Map.of(
                    "reviews", reviews,
                    "averageRating", avg,
                    "totalReviews", reviews.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            reviewService.deleteReview(id, userDetails.getUsername());
            return ResponseEntity.ok("Review deleted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}