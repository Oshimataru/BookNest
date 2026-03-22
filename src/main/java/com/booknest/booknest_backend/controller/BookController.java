package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    // Add new book
    @PostMapping("/add")
    public ResponseEntity<?> addBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "rentPrice", required = false) Double rentPrice,
            @RequestParam("condition") String condition,
            @RequestParam("type") String type,
            @RequestParam("location") String location,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Book book = bookService.addBook(
                    userDetails.getUsername(), title, author,
                    genre, description, price, rentPrice,
                    condition, type, location, image);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Get book by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get my books
    @GetMapping("/my-books")
    public ResponseEntity<?> getMyBooks(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(bookService.getMyBooks(
                    userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Search books
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String keyword) {
        try {
            return ResponseEntity.ok(bookService.searchBooks(keyword));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookService.deleteBook(id, userDetails.getUsername());
            return ResponseEntity.ok("Book deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "rentPrice", required = false) Double rentPrice,
            @RequestParam("condition") String condition,
            @RequestParam("location") String location,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Book book = bookService.updateBook(
                    id, userDetails.getUsername(), title, author,
                    genre, description, price, rentPrice,
                    condition, location, image);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}