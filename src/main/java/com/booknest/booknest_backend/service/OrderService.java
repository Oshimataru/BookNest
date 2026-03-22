package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.Order;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookRepository;
import com.booknest.booknest_backend.repository.OrderRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GamificationService gamificationService;

    @Autowired
    private BookRepository bookRepository;

    // Place order
    public Order placeOrder(String email, Long bookId,
                            String type, String address) {

        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found!"));

        // Can't buy your own book
        if (book.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You cannot order your own book!");
        }

        // Check if book is available
        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available!");
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setBook(book);
        // Convert book type to order type
        String orderType = type.equalsIgnoreCase("SELL") ? "BUY" : type.toUpperCase();
        order.setType(Order.OrderType.valueOf(orderType));
        order.setAddress(address);
        order.setAmount(book.getPrice());

        // Update book status
        // Reduce quantity
        int newQuantity = book.getQuantity() - 1;
        book.setQuantity(newQuantity);

// If quantity is 0 update status
        if (newQuantity <= 0) {
            if (type.equalsIgnoreCase("BUY")) {
                book.setStatus(Book.BookStatus.SOLD);
            } else if (type.equalsIgnoreCase("RENT")) {
                book.setStatus(Book.BookStatus.RENTED);
            } else {
                book.setStatus(Book.BookStatus.EXCHANGED);
            }
        }

        bookRepository.save(book);
        // Award points to buyer
        gamificationService.addPoints(email, 10, "Placed an order!");
        return orderRepository.save(order);
    }

    // Get my orders as buyer
    public List<Order> getMyOrders(String email) {
        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return orderRepository.findByBuyerId(buyer.getId());
    }

    // Get orders for my books as seller
    public List<Order> getSellerOrders(String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return orderRepository.findByBookSellerId(seller.getId());
    }

    // Get order by id
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
    }

    // Update order status
    public Order updateStatus(Long id, String email, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        boolean isSeller = order.getBook().getSeller().getEmail().equals(email);
        boolean isBuyer = order.getBuyer().getEmail().equals(email);

        // Buyer can only set CONFIRMED
        // Seller can set SHIPPED, DELIVERED, CANCELLED
        if (!isSeller && !isBuyer) {
            throw new RuntimeException("You are not authorized!");
        }

        if (isBuyer && !status.equalsIgnoreCase("CONFIRMED")) {
            throw new RuntimeException("Buyer can only confirm order!");
        }

        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }

    // Cancel order
    public Order cancelOrder(Long id, String email) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (!order.getBuyer().getEmail().equals(email)) {
            throw new RuntimeException("You can only cancel your own orders!");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);

        // Make book available again
        order.getBook().setStatus(Book.BookStatus.AVAILABLE);
        bookRepository.save(order.getBook());

        return orderRepository.save(order);
    }
}