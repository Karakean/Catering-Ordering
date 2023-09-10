package com.example.backend.service.interfaces;

import com.example.backend.domain.entity.Order;

public interface OrderService {
    Order create(Order order);
    Order update(Order order);
}
