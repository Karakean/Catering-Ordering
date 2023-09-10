package com.example.backend.service.impl;

import com.example.backend.domain.entity.Catering;
import com.example.backend.repository.CateringRepository;
import com.example.backend.service.interfaces.CateringService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CateringServiceImpl implements CateringService {
    private CateringRepository repository;

    public Catering create(Catering catering) {
        return repository.save(catering);
    }

    public Optional<Catering> findById(Long id) {
        return repository.findById(id);
    }
}
