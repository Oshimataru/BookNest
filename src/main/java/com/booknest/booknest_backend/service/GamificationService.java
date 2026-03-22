package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Badge;
import com.booknest.booknest_backend.model.PointsHistory;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BadgeRepository;
import com.booknest.booknest_backend.repository.PointsHistoryRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class GamificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointsHistoryRepository pointsHistoryRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    // Add points to user
    public void addPoints(String email, int points, String reason) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setPoints(user.getPoints() + points);
        userRepository.save(user);

        PointsHistory history = new PointsHistory();
        history.setUser(user);
        history.setPoints(points);
        history.setReason(reason);
        pointsHistoryRepository.save(history);

        // Check and award badges
        checkAndAwardBadges(user);
    }

    // Check and award badges
    private void checkAndAwardBadges(User user) {
        int points = user.getPoints();

        if (points >= 10 && !badgeRepository.existsByUserIdAndBadgeName(
                user.getId(), "First Steps")) {
            awardBadge(user, "First Steps", "🌟", "Earned your first 10 points!");
        }
        if (points >= 50 && !badgeRepository.existsByUserIdAndBadgeName(
                user.getId(), "Book Lover")) {
            awardBadge(user, "Book Lover", "📚", "Earned 50 points!");
        }
        if (points >= 100 && !badgeRepository.existsByUserIdAndBadgeName(
                user.getId(), "BookNest Pro")) {
            awardBadge(user, "BookNest Pro", "🏆", "Earned 100 points!");
        }
        if (points >= 500 && !badgeRepository.existsByUserIdAndBadgeName(
                user.getId(), "Legend")) {
            awardBadge(user, "Legend", "👑", "Earned 500 points!");
        }
    }

    // Award badge
    private void awardBadge(User user, String name, String icon, String desc) {
        Badge badge = new Badge();
        badge.setUser(user);
        badge.setBadgeName(name);
        badge.setBadgeIcon(icon);
        badge.setBadgeDescription(desc);
        badgeRepository.save(badge);
    }

    // Get my points & badges
    public Map<String, Object> getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<PointsHistory> history = pointsHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());
        List<Badge> badges = badgeRepository.findByUserId(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("totalPoints", user.getPoints());
        result.put("pointsHistory", history);
        result.put("badges", badges);
        return result;
    }

    // Get leaderboard
    public List<User> getLeaderboard() {
        return userRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "points"))
        ).getContent();
    }
}