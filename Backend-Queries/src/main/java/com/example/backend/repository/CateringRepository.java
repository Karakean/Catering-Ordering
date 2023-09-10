package com.example.backend.repository;

import com.example.backend.domain.entity.Catering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateringRepository extends JpaRepository<Catering, Long> {
}

