package com.example.backend.service.interfaces;

import com.example.backend.domain.entity.Catering;

import java.util.List;
import java.util.Optional;

public interface CateringService {
    Catering create(Catering catering);
    List<Catering> findAll();
    Optional<Catering> findById(Long id);
}
