package com.example.backend.service.impl;

import com.example.backend.domain.entity.Order;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.interfaces.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    public Order create(Order order) {
        return repository.save(order);
    }

    public Order update(Order order) {
        return repository.save(order);
    }
}
