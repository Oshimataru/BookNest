package com.booknest.booknest_backend.repository;

import com.booknest.booknest_backend.model.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findByRequesterId(Long requesterId);
    List<ExchangeRequest> findByRequestedBookSellerId(Long sellerId);
}