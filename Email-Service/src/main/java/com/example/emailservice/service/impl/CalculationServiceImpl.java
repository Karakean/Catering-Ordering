package com.example.emailservice.service.impl;

import com.catering.commons.dto.OrderEmailDto;
import com.catering.commons.dto.OrderPositionEmailDto;
import com.example.emailservice.service.interfaces.CalculationService;
import org.springframework.stereotype.Service;

@Service
public class CalculationServiceImpl implements CalculationService {
    @Override
    public double calculatePricePerMonthPerOrderPosition(OrderPositionEmailDto orderPosition) {
        return (double) orderPosition.getQuantity() * orderPosition.getPricePerMonth();
    }

    @Override
    public double calculatePricePerMonthPerOrder(OrderEmailDto order) {
        return order.getOrderPositions().stream()
                .mapToDouble(this::calculatePricePerMonthPerOrderPosition)
                .sum();
    }
}
