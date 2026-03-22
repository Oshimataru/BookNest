package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String genre;

    @Column(length = 1000)
    private String description;

    private Double price;

    private Double rentPrice;

    @Column(name = "book_condition")
    private String condition;

    @Enumerated(EnumType.STRING)
    private BookType type;

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    private String location;

    private String imageUrl;
    private Integer quantity = 1;
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BookType {
        SELL, RENT, EXCHANGE
    }

    public enum BookStatus {
        AVAILABLE, SOLD, RENTED, EXCHANGED
    }

    // Getters
    public Long getId() { return id; }
    public User getSeller() { return seller; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Double getRentPrice() { return rentPrice; }
    public String getCondition() { return condition; }
    public BookType getType() { return type; }
    public BookStatus getStatus() { return status; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setRentPrice(Double rentPrice) { this.rentPrice = rentPrice; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setType(BookType type) { this.type = type; }
    public void setStatus(BookStatus status) { this.status = status; }
    public void setLocation(String location) { this.location = location; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}