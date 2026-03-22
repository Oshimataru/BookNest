package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_requests")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "requested_book_id", nullable = false)
    private Book requestedBook;

    @ManyToOne
    @JoinColumn(name = "offered_book_id", nullable = false)
    private Book offeredBook;

    private String message;

    @Enumerated(EnumType.STRING)
    private ExchangeStatus status = ExchangeStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ExchangeStatus {
        PENDING, ACCEPTED, REJECTED
    }

    // Getters
    public Long getId() { return id; }
    public User getRequester() { return requester; }
    public Book getRequestedBook() { return requestedBook; }
    public Book getOfferedBook() { return offeredBook; }
    public String getMessage() { return message; }
    public ExchangeStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setRequester(User requester) { this.requester = requester; }
    public void setRequestedBook(Book requestedBook) { this.requestedBook = requestedBook; }
    public void setOfferedBook(Book offeredBook) { this.offeredBook = offeredBook; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(ExchangeStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}