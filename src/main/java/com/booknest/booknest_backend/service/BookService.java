package com.booknest.booknest_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GamificationService gamificationService;

    @Autowired
    private Cloudinary cloudinary;

    // Upload image to Cloudinary
    private String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "booknest/books")
        );
        return uploadResult.get("secure_url").toString();
    }

    // Add new book
    public Book addBook(String email, String title, String author,
                        String genre, String description, Double price,
                        Double rentPrice, String condition, String type,
                        String location, Integer quantity, MultipartFile image) throws IOException {

        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Book book = new Book();
        book.setSeller(seller);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setDescription(description);
        book.setPrice(price);
        book.setRentPrice(rentPrice);
        book.setCondition(condition);
        book.setType(Book.BookType.valueOf(type.toUpperCase()));
        book.setLocation(location);

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImage(image);
            book.setImageUrl(imageUrl);
        }
        book.setQuantity(quantity);
        // Award points for listing a book
        gamificationService.addPoints(email, 5, "Listed a book!");
        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by id
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found!"));
    }

    // Get my books
    public List<Book> getMyBooks(String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookRepository.findBySellerId(seller.getId());
    }

    // Search books
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }

    // Delete book
    public void deleteBook(Long id, String email) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found!"));

        if (!book.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own books!");
        }

        bookRepository.delete(book);
    }

    // Update book
    public Book updateBook(Long id, String email, String title,
                           String author, String genre, String description,
                           Double price, Double rentPrice, String condition,
                           String location, MultipartFile image) throws IOException {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found!"));

        if (!book.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own books!");
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setDescription(description);
        book.setPrice(price);
        book.setRentPrice(rentPrice);
        book.setCondition(condition);
        book.setLocation(location);

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImage(image);
            book.setImageUrl(imageUrl);
        }

        return bookRepository.save(book);
    }
}