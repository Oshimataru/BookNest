package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.Book;
import com.booknest.booknest_backend.model.BookClub;
import com.booknest.booknest_backend.model.Review;
import com.booknest.booknest_backend.repository.*;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/home")
@CrossOrigin("*")
public class HomeController {

    private final BookRepository     bookRepo;
    private final UserRepository     userRepo;
    private final ExchangeRepository exchangeRepo;
    private final BookClubRepository clubRepo;
    private final ReviewRepository   reviewRepo;
    private final OrderRepository    orderRepo;

    public HomeController(BookRepository bookRepo,
                          UserRepository userRepo,
                          ExchangeRepository exchangeRepo,
                          BookClubRepository clubRepo,
                          ReviewRepository reviewRepo,
                          OrderRepository orderRepo) {
        this.bookRepo     = bookRepo;
        this.userRepo     = userRepo;
        this.exchangeRepo = exchangeRepo;
        this.clubRepo     = clubRepo;
        this.reviewRepo   = reviewRepo;
        this.orderRepo    = orderRepo;
    }

    /* ── /api/home/stats ──────────────────────────────────────── */
    @PostMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("books",   bookRepo.count());
        stats.put("users",   userRepo.count());
        stats.put("trades",  exchangeRepo.count());
        stats.put("orders",  orderRepo.count());
        stats.put("clubs",   clubRepo.count());
        return stats;
    }

    /* ── /api/home/featured ───────────────────────────────────── */
    @PostMapping("/featured")
    public Map<String, Object> getFeatured() {
        Map<String, Object> data = new LinkedHashMap<>();
        Book book = bookRepo.findTopByOrderByIdDesc();

        if (book != null) {
            data.put("id",     book.getId());
            data.put("title",  book.getTitle());
            data.put("desc",   book.getDescription());
            data.put("image",  book.getImageUrl());
            data.put("author", book.getAuthor());
            data.put("price",  book.getPrice());
            data.put("genre",  book.getGenre());
            data.put("type",   book.getType()   != null ? book.getType().name()   : null);
            data.put("status", book.getStatus() != null ? book.getStatus().name() : null);
        } else {
            data.put("title", "No Books Yet");
            data.put("desc",  "Start adding books 🚀");
        }
        return data;
    }

    /* ── /api/home/books ──────────────────────────────────────── */
    @PostMapping("/books")
    public List<Map<String, Object>> getBooks() {
        List<Book> books = bookRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Book b : books) {

            // ✅ Only include AVAILABLE books
            if (b.getStatus() == null || !b.getStatus().name().equals("AVAILABLE")) {
                continue;
            }

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id",     b.getId());
            map.put("title",  b.getTitle());
            map.put("author", b.getAuthor());
            map.put("desc",   b.getDescription());
            map.put("image",  b.getImageUrl());
            map.put("price",  b.getPrice());
            map.put("genre",  b.getGenre());
            map.put("type",   b.getType()   != null ? b.getType().name()   : null);
            map.put("status", b.getStatus() != null ? b.getStatus().name() : null);

            result.add(map);
        }

        return result;
    }

    /* ── /api/home/genres ─────────────────────────────────────── */
    @PostMapping("/genres")
    public List<Map<String, Object>> getGenres() {
        List<Book> all = bookRepo.findAll();

        // group + count in-memory, no extra query
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (Book b : all) {
            String g = b.getGenre();
            if (g != null && !g.isBlank())
                countMap.merge(g.trim(), 1, Integer::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("name",  e.getKey());
                    m.put("count", e.getValue());
                    m.put("emoji", genreEmoji(e.getKey()));
                    result.add(m);
                });
        return result;
    }

    /* ── /api/home/reviews ────────────────────────────────────── */
    @PostMapping("/reviews")
    public List<Map<String, Object>> getReviews() {
        List<Review> all = reviewRepo.findAll();
        // latest 6 by id
        all.sort(Comparator.comparingLong(Review::getId).reversed());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Review r : all.stream().limit(6).toList()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",      r.getId());
            m.put("rating",  r.getRating());
            m.put("comment", r.getComment());
            if (r.getUser() != null) {
                m.put("username", r.getUser().getName());
                m.put("userId",   r.getUser().getId());
            }
            if (r.getBook() != null) {
                m.put("bookTitle", r.getBook().getTitle());
                m.put("bookId",    r.getBook().getId());
            }
            result.add(m);
        }
        return result;
    }

    /* ── /api/home/clubs ──────────────────────────────────────── */
    @PostMapping("/clubs")
    public List<Map<String, Object>> getClubs() {
        List<BookClub> clubs = clubRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (BookClub c : clubs) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",          c.getId());
            m.put("name",        c.getName());
            m.put("description", c.getDescription());
            try {
                m.put("memberCount",
                        c.getMembers() != null ? c.getMembers().size() : 0);
            } catch (Exception ignored) {
                m.put("memberCount", 0);
            }
            result.add(m);
        }
        return result;
    }

    /* ── helper ───────────────────────────────────────────────── */
    private String genreEmoji(String genre) {
        if (genre == null) return "📚";
        return switch (genre.toLowerCase().trim()) {
            case "fiction"                    -> "📖";
            case "sci-fi", "science fiction"  -> "🚀";
            case "mystery"                    -> "🔍";
            case "romance"                    -> "💕";
            case "fantasy"                    -> "🧙";
            case "horror"                     -> "👻";
            case "self-help", "self help"     -> "💡";
            case "biography"                  -> "👤";
            case "history"                    -> "📜";
            case "thriller"                   -> "⚡";
            case "children"                   -> "🧒";
            case "poetry"                     -> "✍️";
            case "comics"                     -> "🦸";
            default                           -> "📚";
        };
    }
}
