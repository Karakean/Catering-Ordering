package com.example.backend.repository;

import com.example.backend.domain.entity.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {
}
