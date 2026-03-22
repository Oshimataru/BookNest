package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.Review;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookRepository;
import com.booknest.booknest_backend.repository.ReviewRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // Add review
    public Review addReview(String email, Long bookId,
                            int rating, String comment) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found!"));

        // Can't review your own book
        if (book.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You cannot review your own book!");
        }

        // Can't review twice
        if (reviewRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
            throw new RuntimeException("You have already reviewed this book!");
        }

        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5!");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    // Get reviews by book
    public List<Review> getBookReviews(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    // Get average rating
    public Double getAverageRating(Long bookId) {
        Double avg = reviewRepository.getAverageRating(bookId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    // Delete review
    public void deleteReview(Long id, String email) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found!"));

        if (!review.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own review!");
        }

        reviewRepository.delete(review);
    }
}