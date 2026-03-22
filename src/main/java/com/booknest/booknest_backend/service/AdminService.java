package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.Order;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookRepository;
import com.booknest.booknest_backend.repository.OrderRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Delete book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get analytics
    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalBooks = bookRepository.count();
        long totalOrders = orderRepository.count();

        long availableBooks = bookRepository
                .findByStatus(Book.BookStatus.AVAILABLE).size();
        long soldBooks = bookRepository
                .findByStatus(Book.BookStatus.SOLD).size();

        long pendingOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PENDING)
                .count();
        long deliveredOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .count();

        double totalRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                .mapToDouble(o -> o.getAmount() != null ? o.getAmount() : 0)
                .sum();

        analytics.put("totalUsers", totalUsers);
        analytics.put("totalBooks", totalBooks);
        analytics.put("totalOrders", totalOrders);
        analytics.put("availableBooks", availableBooks);
        analytics.put("soldBooks", soldBooks);
        analytics.put("pendingOrders", pendingOrders);
        analytics.put("deliveredOrders", deliveredOrders);
        analytics.put("totalRevenue", totalRevenue);

        return analytics;
    }
}