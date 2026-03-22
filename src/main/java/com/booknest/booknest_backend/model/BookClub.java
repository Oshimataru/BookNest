package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_clubs")
public class BookClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    private String currentBook;

    @ManyToMany
    @JoinTable(name = "club_members",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public User getCreator() { return creator; }
    public String getCurrentBook() { return currentBook; }
    public List<User> getMembers() { return members; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCreator(User creator) { this.creator = creator; }
    public void setCurrentBook(String currentBook) { this.currentBook = currentBook; }
    public void setMembers(List<User> members) { this.members = members; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}