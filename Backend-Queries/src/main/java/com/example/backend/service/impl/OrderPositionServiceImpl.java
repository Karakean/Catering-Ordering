package com.example.backend.service.impl;

import com.example.backend.domain.entity.OrderPosition;
import com.example.backend.repository.OrderPositionRepository;
import com.example.backend.service.interfaces.OrderPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderPositionServiceImpl implements OrderPositionService {
    private OrderPositionRepository repository;

    public OrderPosition create(OrderPosition orderPosition) {
        return repository.save(orderPosition);
    }

    public double calculatePricePerMonthPerOrderPosition(OrderPosition orderPosition) {
        return (double) orderPosition.getQuantity() * orderPosition.getCatering().getPricePerMonth();
    }
}
