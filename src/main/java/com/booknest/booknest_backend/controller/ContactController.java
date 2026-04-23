package com.booknest.booknest_backend.controller;

import com.booknest.booknest_backend.model.ContactMessage;
import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.UserRepository;
import com.booknest.booknest_backend.repository.ContactRepository;
import com.booknest.booknest_backend.service.ContactService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")        
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public ContactController(ContactService contactService,
                             UserRepository userRepository,
                             ContactRepository contactRepository) {
        this.contactService = contactService;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    // ✅ SEND MESSAGE
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> body) {

        try {
            String message = body.get("message");
            String subject = body.get("subject");
            String priority = body.get("priority");

            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User user = userRepository.findByEmail(email).orElseThrow();

            ContactMessage contact = new ContactMessage();
            contact.setName(user.getName());
            contact.setEmail(user.getEmail());
            contact.setMessage(message);
            contact.setSubject(subject);
            contact.setPriority(priority);
            contact.setStatus("PENDING");

            contactService.saveMessage(contact);

            return ResponseEntity.ok("Message Sent");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send message");
        }
    }

    // ✅ ADMIN: GET ALL
    @GetMapping("/all")
    public ResponseEntity<?> getAllMessages() {
        try {
            List<ContactMessage> messages = contactService.getAllMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch messages");
        }
    }

    // ✅ UPDATE STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        try {
            String status = body.get("status");

            ContactMessage msg = contactRepository.findById(id).orElseThrow();
            msg.setStatus(status);

            contactRepository.save(msg);

            return ResponseEntity.ok("Status Updated");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update status");
        }
    }

    // ✅ ADMIN: REPLY
    @PutMapping("/{id}/reply")
    public ResponseEntity<?> replyToMessage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        try {
            String reply = body.get("reply");

            ContactMessage msg = contactRepository.findById(id).orElseThrow();

            msg.setReply(reply);
            msg.setStatus("RESOLVED");
            msg.setSeen(false); // ✅ correct place

            contactRepository.save(msg);

            return ResponseEntity.ok("Reply Sent");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send reply");
        }
    }

    // ✅ USER: GET OWN + MARK SEEN
    @GetMapping("/my")
    public ResponseEntity<?> getMyMessages() {

        try {
            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            List<ContactMessage> list =
                    contactRepository.findByEmail(email);

            // 🔥 mark as seen
            list.forEach(m -> m.setSeen(true));
            contactRepository.saveAll(list);

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}