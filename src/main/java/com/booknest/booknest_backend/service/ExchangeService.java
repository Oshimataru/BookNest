package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.ExchangeRequest;
import com.booknest.booknest_backend.model.Order;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.BookRepository;
import com.booknest.booknest_backend.repository.ExchangeRepository;
import com.booknest.booknest_backend.repository.OrderRepository;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;


    // Send exchange request
    public ExchangeRequest sendRequest(String email, Long requestedBookId,
                                       Long offeredBookId, String message) {

        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Book requestedBook = bookRepository.findById(requestedBookId)
                .orElseThrow(() -> new RuntimeException("Requested book not found!"));

        Book offeredBook = bookRepository.findById(offeredBookId)
                .orElseThrow(() -> new RuntimeException("Offered book not found!"));

        // Can't request your own book
        if (requestedBook.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You cannot request your own book!");
        }

        // Offered book must belong to requester
        if (!offeredBook.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You can only offer your own books!");
        }

        ExchangeRequest request = new ExchangeRequest();
        request.setRequester(requester);
        request.setRequestedBook(requestedBook);
        request.setOfferedBook(offeredBook);
        request.setMessage(message);

        return exchangeRepository.save(request);
    }

    // Get my sent requests
    public List<ExchangeRequest> getMySentRequests(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return exchangeRepository.findByRequesterId(user.getId());
    }

    // Get requests for my books
    public List<ExchangeRequest> getMyReceivedRequests(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return exchangeRepository.findByRequestedBookSellerId(user.getId());
    }

    // Accept request
    public ExchangeRequest acceptRequest(Long id, String email) {
        ExchangeRequest request = exchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found!"));

        if (!request.getRequestedBook().getSeller().getEmail().equals(email)) {
            throw new RuntimeException("Only book owner can accept!");
        }

        request.setStatus(ExchangeRequest.ExchangeStatus.ACCEPTED);

        // Update both books status
        request.getRequestedBook().setStatus(Book.BookStatus.EXCHANGED);
        request.getOfferedBook().setStatus(Book.BookStatus.EXCHANGED);
        bookRepository.save(request.getRequestedBook());
        bookRepository.save(request.getOfferedBook());

        // Create order for requester (gets requested book)
        Order order1 = new Order();
        order1.setBuyer(request.getRequester());
        order1.setBook(request.getRequestedBook());
        order1.setType(Order.OrderType.EXCHANGE);
        order1.setStatus(Order.OrderStatus.CONFIRMED);
        order1.setAmount(0.0);
        order1.setAddress("Exchange");
        orderRepository.save(order1);

        // Create order for seller (gets offered book)
        Order order2 = new Order();
        order2.setBuyer(request.getRequestedBook().getSeller());
        order2.setBook(request.getOfferedBook());
        order2.setType(Order.OrderType.EXCHANGE);
        order2.setStatus(Order.OrderStatus.CONFIRMED);
        order2.setAmount(0.0);
        order2.setAddress("Exchange");
        orderRepository.save(order2);

        return exchangeRepository.save(request);
    }

    // Reject request
    public ExchangeRequest rejectRequest(Long id, String email) {
        ExchangeRequest request = exchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found!"));

        if (!request.getRequestedBook().getSeller().getEmail().equals(email)) {
            throw new RuntimeException("Only book owner can reject!");
        }

        request.setStatus(ExchangeRequest.ExchangeStatus.REJECTED);
        return exchangeRepository.save(request);
    }
}