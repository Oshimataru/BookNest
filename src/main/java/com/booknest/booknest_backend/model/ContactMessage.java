package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean seen = false;
    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
    private String name;
    @Column(length = 1000)
    private String reply;
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    private String email;

    @Column(length = 1000)
    private String message;

    // 🔥 NEW FIELDS
    private String subject;   // Bug, Payment, Feedback
    private String priority;  // LOW, MEDIUM, HIGH

    private String status = "PENDING"; // default

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ Constructors
    public ContactMessage() {}

    public ContactMessage(Long id, String name, String email, String message,
                          String subject, String priority, String status,
                          LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.subject = subject;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ✅ Getters & Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}