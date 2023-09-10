package com.example.backend.repository;

import com.example.backend.domain.entity.Order;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "orders")
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Cacheable(key = "#id")
    Optional<Order> findById(Long id);
}
