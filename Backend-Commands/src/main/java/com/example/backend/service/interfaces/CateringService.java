package com.example.backend.service.interfaces;

import com.example.backend.domain.entity.Catering;

import java.util.Optional;

public interface CateringService {
    Catering create(Catering catering);
    Optional<Catering> findById(Long id);
}
