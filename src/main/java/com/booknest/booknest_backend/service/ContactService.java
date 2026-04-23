package com.booknest.booknest_backend.service;

import com.booknest.booknest_backend.model.ContactMessage;
import com.booknest.booknest_backend.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repo;

    public ContactService(ContactRepository repo) {
        this.repo = repo;
    }

    public ContactMessage saveMessage(ContactMessage msg) {
        return repo.save(msg);
    }
    public List<ContactMessage> getAllMessages() {
        return repo.findAll();
    }

}