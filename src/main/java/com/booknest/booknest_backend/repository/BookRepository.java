package com.booknest.booknest_backend.repository;

import com.booknest.booknest_backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Get all books by a specific seller
    List<Book> findBySellerId(Long sellerId);
    Book findTopByOrderByIdDesc();
    // Get all books by type (SELL, RENT, EXCHANGE)
    List<Book> findByType(Book.BookType type);

    // Get all books by genre
    List<Book> findByGenre(String genre);

    // Search books by title or author
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);

    // Get all available books
    List<Book> findByStatus(Book.BookStatus status);
}