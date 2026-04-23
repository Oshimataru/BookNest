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
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve users at the moment. Please try again shortly.");
        }
    }

    // Delete user
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("We couldn’t delete this user. It may not exist or is linked to other data.");
        }
    }

    // Get all books
    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load books. Please refresh or try again.");
        }
    }

    // Delete book
    public void deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("This book could not be deleted. It might be associated with existing orders.");
        }
    }

    // Get all orders
    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch orders right now. Please try again later.");
        }
    }

    // Get analytics
    public Map<String, Object> getAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();

            long totalUsers = userRepository.count();
            long totalBooks = bookRepository.count();
            long totalOrders = orderRepository.count();

            long availableBooks = bookRepository
                    .findByStatus(Book.BookStatus.AVAILABLE).size();
            long soldBooks = bookRepository
                    .findByStatus(Book.BookStatus.SOLD).size();
            long rentedBooks = bookRepository
                    .findByStatus(Book.BookStatus.RENTED).size();

            long exchangedBooks = bookRepository
                    .findByStatus(Book.BookStatus.EXCHANGED).size();

            List<Order> orders = orderRepository.findAll();
            analytics.put("rentedBooks", rentedBooks);
            analytics.put("exchangedBooks", exchangedBooks);
            long pendingOrders = 0;
            long deliveredOrders = 0;

            for (Order o : orders) {

                if (o.getStatus() == Order.OrderStatus.DELIVERED) {
                    deliveredOrders++;

                } else if (o.getStatus() != Order.OrderStatus.CANCELLED) {
                    pendingOrders++;
                }
            }

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

        } catch (Exception e) {
            throw new RuntimeException("Analytics data is currently unavailable. Please try again in a few moments.");
        }
    }
}