package com.example.emailservice.service.interfaces;

import com.catering.commons.dto.OrderEmailDto;
import com.catering.commons.dto.OrderPositionEmailDto;

public interface CalculationService {
    double calculatePricePerMonthPerOrderPosition(OrderPositionEmailDto orderPosition);
    double calculatePricePerMonthPerOrder(OrderEmailDto order);
}
