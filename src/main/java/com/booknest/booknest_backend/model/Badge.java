package com.booknest.booknest_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String badgeName;
    private String badgeIcon;
    private String badgeDescription;
    private LocalDateTime earnedAt = LocalDateTime.now();

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getBadgeName() { return badgeName; }
    public String getBadgeIcon() { return badgeIcon; }
    public String getBadgeDescription() { return badgeDescription; }
    public LocalDateTime getEarnedAt() { return earnedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setBadgeName(String badgeName) { this.badgeName = badgeName; }
    public void setBadgeIcon(String badgeIcon) { this.badgeIcon = badgeIcon; }
    public void setBadgeDescription(String badgeDescription) { this.badgeDescription = badgeDescription; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }
}