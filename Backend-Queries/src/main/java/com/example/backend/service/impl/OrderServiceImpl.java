package com.example.backend.service.impl;

import com.example.backend.domain.entity.Order;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.interfaces.OrderPositionService;
import com.example.backend.service.interfaces.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderPositionService orderPositionService;

    public Order create(Order order) {
        return repository.save(order);
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public double calculatePricePerMonthPerOrder(Order order) {
        return order.getOrderPositions().stream()
                .mapToDouble(orderPositionService::calculatePricePerMonthPerOrderPosition)
                .sum();
    }
}
