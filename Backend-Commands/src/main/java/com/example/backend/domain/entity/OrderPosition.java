package com.example.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Table(name = "catering_orders")
public class OrderPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    private Catering catering;

    @ManyToOne
    private Order order;
}
