package com.example.backend.service.interfaces;

import com.example.backend.domain.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(Order order);
    List<Order> findAll();
    double calculatePricePerMonthPerOrder(Order order);
}
