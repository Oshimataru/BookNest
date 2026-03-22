package com.booknest.booknest_backend.repository;

import com.booknest.booknest_backend.model.PointsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointsHistoryRepository extends JpaRepository<PointsHistory, Long> {
    List<PointsHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}