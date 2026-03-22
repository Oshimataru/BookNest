package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.BookClub;
import com.booknest.booknest_backend.model.Discussion;
import com.booknest.booknest_backend.service.BookClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = "*")
public class BookClubController {

    @Autowired
    private BookClubService bookClubService;

    // Create club
    @PostMapping("/create")
    public ResponseEntity<?> createClub(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) String currentBook) {
        try {
            BookClub club = bookClubService.createClub(
                    userDetails.getUsername(), name, description, currentBook);
            return ResponseEntity.ok(club);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all clubs
    @GetMapping
    public ResponseEntity<?> getAllClubs() {
        try {
            List<BookClub> clubs = bookClubService.getAllClubs();
            return ResponseEntity.ok(clubs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get club by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getClubById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookClubService.getClubById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Join club
    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinClub(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(bookClubService.joinClub(
                    id, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Leave club
    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveClub(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(bookClubService.leaveClub(
                    id, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Post discussion
    @PostMapping("/{id}/discussion")
    public ResponseEntity<?> postDiscussion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String message) {
        try {
            Discussion discussion = bookClubService.postDiscussion(
                    id, userDetails.getUsername(), message);
            return ResponseEntity.ok(discussion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get discussions
    @GetMapping("/{id}/discussions")
    public ResponseEntity<?> getDiscussions(@PathVariable Long id) {
        try {
            List<Discussion> discussions = bookClubService.getDiscussions(id);
            return ResponseEntity.ok(discussions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}