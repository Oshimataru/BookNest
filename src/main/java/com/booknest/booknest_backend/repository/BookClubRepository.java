package com.booknest.booknest_backend.repository;

import com.booknest.booknest_backend.model.BookClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookClubRepository extends JpaRepository<BookClub, Long> {
    List<BookClub> findByCreatorId(Long creatorId);
}