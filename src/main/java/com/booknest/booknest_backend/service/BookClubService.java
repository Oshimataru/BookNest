package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.BookClub;
import com.booknest.booknest_backend.model.Discussion;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookClubRepository;
import com.booknest.booknest_backend.repository.DiscussionRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookClubService {

    @Autowired
    private BookClubRepository bookClubRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    // Create club
    public BookClub createClub(String email, String name,
                               String description, String currentBook) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BookClub club = new BookClub();
        club.setName(name);
        club.setDescription(description);
        club.setCreator(creator);
        club.setCurrentBook(currentBook);
        club.setMembers(new ArrayList<>(List.of(creator)));

        return bookClubRepository.save(club);
    }

    // Get all clubs
    public List<BookClub> getAllClubs() {
        return bookClubRepository.findAll();
    }

    // Get club by id
    public BookClub getClubById(Long id) {
        return bookClubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found!"));
    }

    // Join club
    public BookClub joinClub(Long clubId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BookClub club = bookClubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found!"));

        if (!club.getMembers().contains(user)) {
            club.getMembers().add(user);
            bookClubRepository.save(club);
        }

        return club;
    }

    // Leave club
    public BookClub leaveClub(Long clubId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BookClub club = bookClubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found!"));

        club.getMembers().remove(user);
        return bookClubRepository.save(club);
    }

    // Post discussion
    public Discussion postDiscussion(Long clubId, String email, String message) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BookClub club = bookClubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found!"));

        Discussion discussion = new Discussion();
        discussion.setClub(club);
        discussion.setUser(user);
        discussion.setMessage(message);

        return discussionRepository.save(discussion);
    }

    // Get discussions
    public List<Discussion> getDiscussions(Long clubId) {
        return discussionRepository.findByClubIdOrderByCreatedAtAsc(clubId);
    }
}